/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import kotx.minecraft.libs.flylib.asFlyLibContext
import kotx.minecraft.libs.flylib.command.internal.Argument
import kotx.minecraft.libs.flylib.command.internal.CommandDefault
import kotx.minecraft.libs.flylib.command.internal.Permission
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
                val outer = getArgumentBuilder(usage.args.first())

                setupArgument(usage.action, usage.args.first(), outer, this, depthMap[this]!!)

                usage.args.drop(1).forEach {
                    val inner = getArgumentBuilder(it)
                    setupArgument(usage.action, it, inner, this, depthMap[this]!!)
                    outer.then(inner)
                }

                base.then(outer)
            }

            children.forEach { child ->
                val childBase = LiteralArgumentBuilder.literal<CommandListenerWrapper?>(child.name).requires { child.validate(it.bukkitSender) }
                child.handle(childBase)
                base.then(childBase)

                child.aliases.forEach {
                    val childAliasBase =
                        LiteralArgumentBuilder.literal<CommandListenerWrapper?>(child.name).requires { child.validate(it.bukkitSender) }
                    child.handle(childAliasBase)
                    base.then(childAliasBase)
                }
            }
        }

        val base = LiteralArgumentBuilder
            .literal<CommandListenerWrapper?>(name)
            .requires { command.validate(it.bukkitSender) }
            .executes {
                val userInput = it.input.replaceFirst("/", "").split(" ")
                command.handleExecute(it.source.bukkitSender, userInput[0], userInput.drop(1).toTypedArray())
                1
            }

        command.handle(base)

        ((Bukkit.getServer() as CraftServer).server as MinecraftServer).commandDispatcher.a().register(base)
    }

    private fun getArgumentBuilder(argument: Argument): ArgumentBuilder<CommandListenerWrapper, *> = if (argument.type == null)
        LiteralArgumentBuilder.literal(argument.name)
    else
        RequiredArgumentBuilder.argument(argument.name, argument.type)

    private fun setupArgument(
        action: (kotx.minecraft.libs.flylib.command.CommandContext.() -> Unit)?,
        argument: Argument,
        cursor: ArgumentBuilder<CommandListenerWrapper, *>,
        command: Command,
        depth: Int
    ) {
        cursor.requires {
            val validPermission = when (argument.permission) {
                Permission.OP -> it.bukkitSender.isOp
                Permission.NOT_OP -> !it.bukkitSender.isOp
                Permission.EVERYONE -> true
            }

            if (!validPermission) return@requires false

            val playerOnly = argument.playerOnly
            val validSender = !playerOnly || playerOnly && it.bukkitSender is Player

            if (!validSender) return@requires false

            true
        }

        if (cursor is RequiredArgumentBuilder<CommandListenerWrapper, *> && argument.tabComplete != null) cursor.suggests { ctx, builder ->
            argument.tabComplete.invoke(ctx.asFlyLibContext(command, depth)).forEach {
                builder.suggest(it)
            }

            builder.buildFuture()
        }


        cursor.executes {
            val ctx = it as CommandContext<CommandListenerWrapper>
            if (action != null) {
                action.invoke(ctx.asFlyLibContext(command, depth))
            } else {
                val userInput = ctx.input.replaceFirst("/", "").split(" ")
                command.handleExecute(ctx.source.bukkitSender, userInput[0], userInput.drop(1 + depth).toTypedArray())
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