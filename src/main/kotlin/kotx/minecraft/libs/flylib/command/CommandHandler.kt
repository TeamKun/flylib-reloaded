/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command

import kotx.minecraft.libs.flylib.command.internal.CommandCompletion
import kotx.minecraft.libs.flylib.command.internal.CommandDefault
import kotx.minecraft.libs.flylib.command.internal.Permission
import kotx.minecraft.libs.flylib.get
import org.bukkit.command.CommandSender
import org.bukkit.permissions.PermissionDefault
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.Logger

class CommandHandler(
    private val commands: List<Command>,
    val commandCompletion: CommandCompletion,
    val commandDefault: CommandDefault
) : KoinComponent {
    private val plugin: JavaPlugin by inject()
    private val logger: Logger by inject()

    fun initialize() {
        logger.info("Registering command handling permission")
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
        commands.forEach {
            plugin.server.commandMap.register("flylib", object : org.bukkit.command.Command(it.name) {
                override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
                    handleCommand(sender, label, args)
                    return true
                }

                override fun tabComplete(
                    sender: CommandSender,
                    alias: String,
                    args: Array<out String>
                ) = handleTabComplete(sender, alias, args).toMutableList()
            }.apply {
                label = it.name
                aliases = it.aliases
                description = it.description
                permission = when (it.permission) {
                    Permission.OP -> "flylib.op"
                    Permission.NOT_OP -> "flylib.notop"
                    Permission.EVERYONE -> "flylib.everyone"
                }
                permissionMessage = "You can't execute ${it.name} command! (permission required)"
            })
            logger.info("Registered command: ${it.name}")
        }
        logger.info("Finished registering commands.")
    }

    private fun handleCommand(
        sender: CommandSender,
        label: String,
        args: Array<out String>
    ) {
        val cmd = commands[label] ?: return
        cmd.handleExecute(sender, label, args)
    }

    private fun handleTabComplete(
        sender: CommandSender,
        alias: String,
        args: Array<out String>
    ): List<String> {
        val cmd = commands[alias] ?: return emptyList()

        return cmd.handleTabComplete(sender, alias, args)
    }

    class Builder {
        private val commands = mutableListOf<Command>()

        private var commandCompletion: CommandCompletion = CommandCompletion.Builder().build()
        private var commandDefault: CommandDefault = CommandDefault.Builder().build()

        /**
         * Configure settings for tab storage. See Javadoc in CommandCompletion below for details.
         * @see CommandCompletion
         */
        fun completion(commandCompletion: CommandCompletion): Builder {
            this.commandCompletion = commandCompletion
            return this
        }

        /**
         * Configure settings for tab storage. See Javadoc in CommandCompletion below for details.
         * This is a method that corresponds to Kotlin's apply builder pattern.
         * @see CommandCompletion
         */
        fun completion(init: CommandCompletion.Builder.() -> Unit): Builder {
            commandCompletion = CommandCompletion.Builder().apply(init).build()
            return this
        }

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
            commandCompletion,
            commandDefault
        )
    }
}