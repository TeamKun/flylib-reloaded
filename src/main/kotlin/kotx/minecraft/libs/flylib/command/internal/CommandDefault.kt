/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command.internal

import kotx.minecraft.libs.flylib.*
import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandContext
import net.kyori.adventure.text.format.TextDecoration
import java.awt.Color

class CommandDefault(
    val description: String,
    val permission: Permission,
    val playerOnly: Boolean,
    val invalidCommandMessage: (Command) -> String,
    val sendHelp: CommandContext.() -> Unit,
) {
    class Builder {
        private var description: String = "No description provided."
        private var permission: Permission = Permission.OP
        private var playerOnly: Boolean = false
        private var invalidCommandMessage: (Command) -> String = {
            "You can't execute ${it.name} command!"
        }
        private var sendHelp: CommandContext.() -> Unit = {
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

        fun description(defaultDescription: String): Builder {
            description = defaultDescription
            return this
        }

        fun permission(defaultPermission: Permission): Builder {
            permission = defaultPermission
            return this
        }

        fun playerOnly(defaultPlayerOnly: Boolean): Builder {
            playerOnly = defaultPlayerOnly
            return this
        }

        fun help(defaultSendHelp: CommandContext.() -> Unit): Builder {
            sendHelp = defaultSendHelp
            return this
        }

        fun invalidMessage(invalidCommandMessage: (Command) -> String): Builder {
            this.invalidCommandMessage = invalidCommandMessage
            return this
        }

        fun build() = CommandDefault(
            description, permission, playerOnly, invalidCommandMessage, sendHelp
        )
    }
}