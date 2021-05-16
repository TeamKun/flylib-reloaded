/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command

import com.mojang.brigadier.LiteralMessage
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import kotx.minecraft.libs.flylib.asFlyLibContext
import kotx.minecraft.libs.flylib.command.internal.Argument
import kotx.minecraft.libs.flylib.command.internal.CommandDefault
import kotx.minecraft.libs.flylib.command.internal.Permission
import kotx.minecraft.libs.flylib.command.internal.Usage
import net.minecraft.server.v1_16_R3.CommandListenerWrapper
import net.minecraft.server.v1_16_R3.MinecraftServer
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_16_R3.CraftServer
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionDefault
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.Logger


class CommandHandler(
    private val commands: List<Command>,
    val commandDefault: CommandDefault
) : KoinComponent {
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
                        child.apply { it.asFlyLibContext(child, depthMap[child] ?: 0).execute() }
                        1
                    }
                child.handle(childBase)
                base.then(childBase)

                child.aliases.forEach {
                    val childAliasBase = LiteralArgumentBuilder
                        .literal<CommandListenerWrapper?>(it)
                        .requires { child.validate(it.bukkitSender) }
                        .executes {
                            child.apply { it.asFlyLibContext(child, depthMap[child] ?: 0).execute() }
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
                command.apply { it.asFlyLibContext(command, 0).execute() }
                1
            }

        command.handle(base)

        ((Bukkit.getServer() as CraftServer).server as MinecraftServer).commandDispatcher.a().register(base)
    }

    private fun getArgumentBuilder(argument: Argument): ArgumentBuilder<CommandListenerWrapper, *> = if (argument.type == null)
        LiteralArgumentBuilder.literal(argument.name)
    else
        RequiredArgumentBuilder.argument(argument.name, argument.type)

    private fun ArgumentBuilder<CommandListenerWrapper, *>.setupArgument(
        usage: Usage,
        argument: Argument,
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
            argument.tabComplete.invoke(ctx.asFlyLibContext(command, depth)).filter { it.content.startsWith(builder.remaining, true) }.forEach {
                val toolTipMessage = it.tooltip?.let { LiteralMessage(it) }
                builder.suggest(it.content, toolTipMessage)
            }

            builder.buildFuture()
        }


        executes {
            val ctx = it as CommandContext<CommandListenerWrapper>
            if (usage.action != null) usage.action.invoke(ctx.asFlyLibContext(command, depth)) else command.apply {
                ctx.asFlyLibContext(command, depth).execute()
            }

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
        fun defaultConfiguration(commandDefault: CommandDefault): Builder {
            this.commandDefault = commandDefault
            return this
        }

        /**
         * Changes the default command settings. See CommandDefault below for details.
         * This is a method that corresponds to Kotlin's apply builder pattern.
         * @see CommandDefault
         */
        fun defaultConfiguration(init: CommandDefault.Builder.() -> Unit): Builder {
            commandDefault = CommandDefault.Builder().apply(init).build()
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

        fun build() = CommandHandler(
            commands,
            commandDefault
        )
    }
}