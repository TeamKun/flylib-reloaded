/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command.internal

import dev.kotx.flylib.command.*
import dev.kotx.flylib.utils.*
import net.kyori.adventure.text.event.*
import net.kyori.adventure.text.format.*
import java.awt.*

object CommandDefault {

    /**
     * Descriptive text that is specified by default when nothing is specified
     */
    private var description: String = "No description provided."

    /**
     * Privileges specified by default when nothing is specified
     */
    private var permission: Permission = Permission.EVERYONE

    /**
     * Whether only the player specified by default can execute when nothing is specified
     */
    private var playerOnly: Boolean = false

    /**
     * The method that will be executed when you call sendHelp() when nothing is implemented.
     */
    private var sendHelp: CommandContext.Action = CommandContext.Action {
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
            append("--------------------------------------------------\n", Color.DARK_GRAY)
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

            when {
                command.description.lines().size == 1 -> {
                    append(" - ", subColor)
                    append(command.description, subColor)
                }
                command.description.lines().size > 1 -> {
                    appendln()
                    append(command.description, subColor)
                }
            }

            appendln()

            fun Command.handleParent(current: String): String = if (parent != null)
                parent!!.handleParent("$name $current")
            else
                "/$name $current"

            when (command.usages.size) {
                0 -> {
                }
                1 -> {
                    val usage = command.usages.first()

                    append("Usage: ", Color.WHITE, TextDecoration.BOLD)

                    append(command.handleParent(usage.args.joinToString(" ") { "<${it.name}>" }), mainColor)

                    if (usage.description.isNotEmpty()) {
                        append(" - ", subColor)
                        append(usage.description, subColor)
                    }

                    appendln()
                }
                else -> {
                    appendln()
                    append("# ", Color.WHITE)
                    append("Usages:\n", highlightTextColor, TextDecoration.BOLD)

                    command.usages.associate { it ->
                        command.handleParent(it.args.joinToString(" ") {
                            if (it is Argument.Literal)
                                it.name
                            else
                                "<${it.name}>"
                        }) to it.description
                    }.forEach { (usage, description) ->
                        append(usage, mainColor)
                        if (description.isNotEmpty()) {
                            append(" - ", subColor)
                            append(description, subColor)
                        }
                        appendln()
                    }
                }
            }

            when (command.examples.size) {
                0 -> {

                }

                1 -> {
                    append("Example: ", Color.WHITE, TextDecoration.BOLD)
                    append("/${command.examples.first()}\n", mainColor)
                    appendln()
                }

                else -> {
                    appendln()
                    append("# ", Color.WHITE)
                    append("Examples:\n", highlightTextColor, TextDecoration.BOLD)
                    command.examples.map {
                        "/$it".component(mainColor)
                            .clickEvent(ClickEvent.runCommand(it))
                            .hoverEvent(HoverEvent.showText("Click to run!".component(Color.LIGHT_GRAY)))
                    }.joint("\n".component()) {
                        append(it)
                    }
                    appendln()
                }
            }

            if (command.children.isNotEmpty()) {
                command.children.map { "    ${it.name}".component(highlightTextColor) }.joint("\n".component()) {
                    append(it)
                }
                appendln()
            }

            append("--------------------------------------------------", Color.DARK_GRAY)
        }
    }

    /**
     * Descriptive text that is specified by default when nothing is specified
     */
    fun description(defaultDescription: String): CommandDefault {
        description = defaultDescription
        return this
    }

    fun getDescription() = description

    /**
     * Privileges specified by default when nothing is specified
     */
    fun permission(defaultPermission: Permission): CommandDefault {
        permission = defaultPermission
        return this
    }

    fun getPermission() = permission

    /**
     * Whether only the player specified by default can execute when nothing is specified
     */
    fun playerOnly(defaultPlayerOnly: Boolean): CommandDefault {
        playerOnly = defaultPlayerOnly
        return this
    }

    fun isPlayerOnly() = playerOnly

    /**
     * The method that will be executed when you call sendHelp() when nothing is implemented.
     */
    fun help(defaultSendHelp: CommandContext.Action): CommandDefault {
        sendHelp = defaultSendHelp
        return this
    }

    fun getHelp() = sendHelp

    fun interface Action {
        fun CommandDefault.initialize()
    }
}