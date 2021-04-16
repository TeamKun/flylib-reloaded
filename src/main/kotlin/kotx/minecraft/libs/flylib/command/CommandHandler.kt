/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command

import kotx.minecraft.libs.flylib.*
import kotx.minecraft.libs.flylib.command.internal.CommandCompletion
import kotx.minecraft.libs.flylib.command.internal.Permission
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.command.CommandSender
import org.bukkit.permissions.PermissionDefault
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.Logger
import java.awt.Color

class CommandHandler(
    private val commands: List<Command>,
    val commandCompletion: CommandCompletion,
    val defaultDescription: String,
    val defaultPermission: Permission,
    val defaultPlayerOnly: Boolean,
    val invalidCommandMessage: (Command) -> String,
    val defaultSendHelp: CommandContext.() -> Unit
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

        /**
         * Description specified by default when nothing is specified when creating a Command instance
         */
        private var defaultDescription: String = ""

        /**
         * Privileges specified by default if nothing is specified when creating a command instance
         */
        private var defaultPermission: Permission = Permission.OP


        /**
         * Whether or not only the player specified by default can be executed if nothing is specified when creating a command instance
         */
        private var defaultPlayerOnly: Boolean = false

        /**
         * Message sent when the command is unavailable
         */
        private var invalidCommandMessage: (Command) -> String = {
            "You can't execute ${it.name} command!"
        }

        /**
         * Contents of send Help used by default when not overridden
         */
        private var defaultSendHelp: CommandContext.() -> Unit = {
            var fullName = command.name
            fun Command.getFullName() {
                if (parent != null) {
                    fullName = "${parent!!.name} $fullName"
                    parent!!.getFullName()
                }
            }
            command.getFullName()

            sender.send {
                val nameColor = Color.RED
                val mainColor = Color.CYAN
                val subColor = Color.LIGHT_GRAY
                val highlightTextColor = Color.ORANGE
                append("-----------------------------------\n", Color.DARK_GRAY)
                append("/$fullName", nameColor)
                if (command.aliases.isNotEmpty()) {
                    append("(", Color.WHITE)
                    command.aliases.forEachIndexed { i, it ->
                        append(it, mainColor)
                        if (i < command.aliases.size - 1)
                            append(", ", subColor)
                    }
                    append(")", Color.WHITE)
                }

                if (command.description.isNotEmpty()) {
                    append(" - ", subColor)
                    append(command.description, subColor)
                }

                append("\n")

                fun Command.handleParent(current: String): String = if (parent != null)
                    parent!!.handleParent("${parent!!.name} $current")
                else
                    "/$current"

                when (command.usages.size) {
                    0 -> {
                    }
                    1 -> {
                        val usage = command.usages.first()

                        append("Usage: ", Color.WHITE, decoration = TextDecoration.BOLD)
                        append(command.handleParent(usage.context), mainColor)
                        if (usage.description.isNotEmpty()) {
                            append(" - ", subColor)
                            append(usage.description, subColor)
                        }

                        append("\n")
                    }
                    else -> {
                        appendln()
                        append("# ", Color.WHITE)
                        append("Usages:\n", highlightTextColor, decoration = TextDecoration.BOLD)

                        command.usages.associate { command.handleParent(it.context) to it.description }.forEach { (usage, description) ->
                            append(usage, mainColor)
                            if (description.isNotEmpty()) {
                                append(" - ", subColor)
                                append(description, subColor)
                            }
                            append("\n")
                        }
                    }
                }

                when (command.examples.size) {
                    0 -> {

                    }

                    1 -> {
                        append("Example: ", Color.WHITE, decoration = TextDecoration.BOLD)
                        append("/${command.examples.first()}\n", mainColor)
                    }

                    else -> {
                        appendln()
                        append("# ", Color.WHITE)
                        append("Examples:\n", highlightTextColor, decoration = TextDecoration.BOLD)
                        command.examples.map { "/$it".asTextComponent(mainColor) }.joint("\n".asTextComponent()) {
                            append(it)
                        }
                    }
                }

                if (command.children.isNotEmpty()) {
                    command.children.map { "    ${it.name}".asTextComponent(highlightTextColor) }.joint("\n".asTextComponent()) {
                        append(it)
                    }
                    appendln()
                }

                append("-----------------------------------", Color.DARK_GRAY)
            }
        }

        fun completion(commandCompletion: CommandCompletion): Builder {
            this.commandCompletion = commandCompletion
            return this
        }

        fun completion(init: CommandCompletion.Builder.() -> Unit): Builder {
            commandCompletion = CommandCompletion.Builder().apply(init).build()
            return this
        }

        /**
         * Registers the specified command. It is not necessary to register commands or permissions in plugin.yml.
         */
        fun registerCommand(command: Command): Builder {
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
            defaultDescription,
            defaultPermission,
            defaultPlayerOnly,
            invalidCommandMessage,
            defaultSendHelp
        )
    }
}