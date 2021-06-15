/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command

import com.mojang.brigadier.*
import com.mojang.brigadier.builder.*
import com.mojang.brigadier.context.CommandContext
import dev.kotx.flylib.*
import dev.kotx.flylib.command.internal.*
import dev.kotx.flylib.command.internal.Permission
import net.minecraft.server.v1_16_R3.*
import org.bukkit.*
import org.bukkit.craftbukkit.v1_16_R3.*
import org.bukkit.entity.*
import org.bukkit.permissions.*
import org.bukkit.plugin.java.*
import org.koin.core.component.*
import org.slf4j.*
import kotlin.collections.set


class CommandHandler(
    private val commands: List<Command>,
    val commandDefault: CommandDefault
) : FlyLibComponent {
    private val plugin: JavaPlugin by inject()
    private val logger: Logger by inject()

    fun initialize() {
        plugin.server.pluginManager.addPermission(
            org.bukkit.permissions.Permission(
                "flylib.op",
                PermissionDefault.OP
            )
        )
        plugin.server.pluginManager.addPermission(
            org.bukkit.permissions.Permission(
                "flylib.notop",
                PermissionDefault.NOT_OP
            )
        )
        plugin.server.pluginManager.addPermission(
            org.bukkit.permissions.Permission(
                "flylib.everyone",
                PermissionDefault.TRUE
            )
        )

        logger.info("Registering commands: (${commands.size})")
        commands.forEach { command ->
            register(
                command,
                command.name
            )

            command.aliases.forEach {
                register(
                    command,
                    it
                )
            }
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, {
            commands.forEach { cmd ->
                plugin.server.commandMap.getCommand(cmd.name)?.permission = when (cmd.permission) {
                    Permission.OP -> "flylib.op"
                    Permission.NOT_OP -> "flylib.notop"
                    Permission.EVERYONE -> "flylib.everyone"
                }


                cmd.aliases.forEach {
                    plugin.server.commandMap.getCommand(it)?.permission = when (cmd.permission) {
                        Permission.OP -> "flylib.op"
                        Permission.NOT_OP -> "flylib.notop"
                        Permission.EVERYONE -> "flylib.everyone"
                    }
                }
            }
        }, 0L)
    }

    private fun register(
        command: Command,
        name: String
    ) {

        val depthMap = mutableMapOf<Command, Int>()
        var depth = 0

        fun List<Command>.handleDepthMap() {
            if (!isEmpty()) {
                depth++
                forEach { depthMap[it] = depth }
                flatMap { it.children }.handleDepthMap()
            }
        }

        command.children.handleDepthMap()

        fun Command.handle(base: LiteralArgumentBuilder<CommandListenerWrapper>) {
            usages.forEach { usage ->
                var usageBase: ArgumentBuilder<CommandListenerWrapper, *>? = null

                usage.args.reversed().forEach {
                    val ch = usageBase
                    usageBase = getArgumentBuilder(it)
                    if (ch != null)
                        usageBase?.then(ch)
                    usageBase?.setupArgument(usage, it, this, depthMap[this] ?: 0)
                }

                base.then(usageBase)
            }

            children.forEach { child ->
                val childBase = LiteralArgumentBuilder
                    .literal<CommandListenerWrapper?>(child.name)
                    .requires { child.validate(it.bukkitSender) }
                    .executes {
                        child.apply { it.asFlyLibContext(child, emptyList(), depthMap[child] ?: 0).execute() }
                        1
                    }
                child.handle(childBase)
                base.then(childBase)

                child.aliases.forEach {
                    val childAliasBase = LiteralArgumentBuilder
                        .literal<CommandListenerWrapper?>(it)
                        .requires { child.validate(it.bukkitSender) }
                        .executes {
                            child.apply { it.asFlyLibContext(child, emptyList(), depthMap[child] ?: 0).execute() }
                            1
                        }
                    child.handle(childAliasBase)
                    base.then(childAliasBase)
                }
            }
        }

        val base = LiteralArgumentBuilder
            .literal<CommandListenerWrapper?>(name)
            .requires { command.validate(it.bukkitSender) }
            .executes {
                command.apply { it.asFlyLibContext(command, emptyList(), 0).execute() }
                1
            }

        command.handle(base)

        ((Bukkit.getServer() as CraftServer).server as MinecraftServer).commandDispatcher.a().register(base)
    }

    private fun getArgumentBuilder(argument: Argument<*>): ArgumentBuilder<CommandListenerWrapper, *> = if (argument.type == null)
        LiteralArgumentBuilder.literal(argument.name)
    else
        RequiredArgumentBuilder.argument(argument.name, argument.type)

    private fun ArgumentBuilder<CommandListenerWrapper, *>.setupArgument(
        usage: Usage,
        argument: Argument<*>,
        command: Command,
        depth: Int
    ) {
        requires {
            val validPermission = when (usage.permission ?: command.permission) {
                Permission.OP -> it.bukkitSender.isOp
                Permission.NOT_OP -> !it.bukkitSender.isOp
                Permission.EVERYONE -> true
            }

            if (!validPermission) return@requires false

            val playerOnly = usage.playerOnly ?: command.playerOnly
            val validSender = !playerOnly || playerOnly && it.bukkitSender is Player

            if (!validSender) return@requires false

            true
        }


        if (this is RequiredArgumentBuilder<CommandListenerWrapper, *> && argument.tabComplete != null) suggests { ctx, builder ->
            argument.tabComplete.run {
                val context = ctx.asFlyLibContext(command, usage.args.toList(), depth)
                SuggestionBuilder(
                    context.command,
                    context.plugin,
                    context.sender,
                    context.message,
                    context.args,
                    context.typedArgs
                ).run {
                    complete()
                    build()
                }
            }.forEach {
                val toolTipMessage = it.tooltip?.let { LiteralMessage(it) }
                builder.suggest(it.content, toolTipMessage)
            }

            builder.buildFuture()
        }

        executes {
            val ctx = (it as CommandContext<CommandListenerWrapper>).asFlyLibContext(command, usage.args.toList(), depth)

            usage.action?.apply { ctx.execute() } ?: command.apply { ctx.execute() }

            1
        }
    }

    class Builder {
        private val commands = mutableListOf<Command>()

        private var commandDefault: CommandDefault = CommandDefault.Builder().build()

        /**
         * Changes the default command settings. See CommandDefault below for details.
         * @see CommandDefault
         */
        fun defaultConfiguration(action: CommandDefaultAction): Builder {
            this.commandDefault = CommandDefault.Builder().apply { action.apply { initialize() } }.build()
            return this
        }

        /**
         * Registers the specified command. It is not necessary to register commands or permissions in plugin.yml.
         */
        fun register(command: Command): Builder {
            fun List<Command>.setParent(parent: Command): Unit = forEach {
                it.parent = parent
                it.children.setParent(it)
            }

            command.children.setParent(command)
            commands.add(command)
            return this
        }

        /**
         * Build CommandHandler
         *
         * @return CommandHandler Instance
         */
        fun build() = CommandHandler(
            commands,
            commandDefault
        )
    }
}

fun interface CommandHandlerAction {
    fun CommandHandler.Builder.initialize()
}