/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command

import dev.kotx.flylib.FlyLib
import dev.kotx.flylib.command.arguments.LiteralArgument
import dev.kotx.flylib.util.ComponentBuilderAction
import dev.kotx.flylib.util.component
import dev.kotx.flylib.util.fail
import dev.kotx.flylib.util.joint
import dev.kotx.flylib.util.message
import dev.kotx.flylib.util.pluginMessage
import dev.kotx.flylib.util.pluginMessageFail
import dev.kotx.flylib.util.pluginMessageSuccess
import dev.kotx.flylib.util.pluginMessageWarn
import dev.kotx.flylib.util.success
import dev.kotx.flylib.util.warn
import net.kyori.adventure.text.Component
import org.bukkit.Server
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.awt.Color

/**
 *  The context when the command is executed.
 *  It is passed as an argument or receiver when executing a command or completing a tab.
 *  This class holds information when executing a command or completing a tab, which can be retrieved by the plug-in that registered the command, the command itself, the sender of the command, etc. It can also send a message to the executor of the command, display usage instructions, etc.
 */
class CommandContext<T>(
    val flyLib: FlyLib<T>,
    val config: T?,
    /**
     * The command that has been executed or is about to be executed.
     */
    val command: Command<T>,
    /**
     * The executor of the command or the entity that is about to execute it (may not be the player).
     */
    val sender: CommandSender,
    /**
     * The world where the command was executed
     */
    val world: World?,
    /**
     * The server on which the plugin was run
     */
    val server: Server,
    /**
     * Command input message. (includes slash)
     */
    val message: String,
    depth: Int,
    val typedArgs: List<Any?> = emptyList()
) {
    val plugin = flyLib.plugin

    /**
     * Executed command sender. (Limited to the player, but null if executed by someone other than the player.)
     */
    val player = sender as? Player

    /**
     * Command arguments. The remaining arguments except the beginning of the command are assigned.
     * These are all returned as strings.
     * If you need parsed values, see typedArgs.
     *
     * ## Example
     * ### Basic command
     * /command <number>
     *     args: <number>
     *
     * ### Nested command
     * /parent children <arg1> <arg2>
     *     args: <arg1>, <arg2>
     *
     * ### Usage argument
     * /command <literal> <text> <number> <entity>
     *     args: <literal>, <text>, <number>, <entity>
     *
     * @see typedArgs
     */

    val args = message.replaceFirst("^/".toRegex(), "").split(" ").drop(depth)

    /**
     * send string message
     */
    fun message(text: String) = sender.message(text)

    /**
     * send string colored message
     */
    fun message(text: String, color: Color) = sender.message(text, color)

    /**
     * send component
     */
    fun message(component: Component) = sender.message(component)

    /**
     * send component via builder
     */
    fun message(builder: ComponentBuilderAction) = sender.message(builder)

    /**
     * send green string
     */
    fun success(text: String) = sender.success(text)

    /**
     * send yellow string
     */
    fun warn(text: String) = sender.warn(text)

    /**
     * send red string
     */
    fun fail(text: String) = sender.fail(text)

    /**
     * send string with plugin name
     */
    fun pluginMessage(text: String) = sender.pluginMessage(plugin, text)

    /**
     * send colored string with plugin name
     */
    fun pluginMessage(text: String, color: Color) = sender.pluginMessage(plugin, text, color)

    /**
     * send component with plugin name
     */
    fun pluginMessage(component: Component) = sender.pluginMessage(plugin, component)

    /**
     * send component via builder with plugin name
     */
    fun pluginMessage(builder: ComponentBuilderAction) = sender.pluginMessage(plugin, builder)

    /**
     * send green string with plugin name
     */
    fun pluginMessageSuccess(text: String) = sender.pluginMessageSuccess(plugin, text)

    /**
     * send yellow string with plugin name
     */
    fun pluginMessageWarn(text: String) = sender.pluginMessageWarn(plugin, text)

    /**
     * send red string with plugin name
     */
    fun pluginMessageFail(text: String) = sender.pluginMessageFail(plugin, text)

    fun updateConfig(action: T.() -> Unit) = flyLib.updateConfig(action)

    /**
     * Display the information of the command in this context in the sender of this context. (It will also include usages and examples.)
     */
    fun sendHelp() {
        val fullName = command.fullCommand.joinToString(" ") { it.name }
        message {
            appendln("--------------------------------------------------", Color.DARK_GRAY)
            append("/", Color(0, 80, 200))
            append(fullName, Color(0, 123, 255))

            if (command.description != null) {
                append(" - ", Color.GRAY)
                append(command.description!!, Color.WHITE)
            }

            appendln()

            command.children.forEach {
                append("    ")
                append(it.name, Color.ORANGE)
                if (it.description != null) {
                    append(" - ", Color.GRAY)
                    append(it.description!!, Color.WHITE)
                }
                appendln()
            }

            if (command.aliases.isNotEmpty()) {
                appendln()
                append("Aliases", Color.RED)
                append(":", Color.GRAY)
                appendln()

                command.aliases.joint("\n".component(Color.DARK_GRAY)) { "    $it".component() }.forEach { +it }

                appendln()
            }

            if (command.usages.isNotEmpty()) {
                appendln()
                append("Usages", Color.RED)
                append(":", Color.GRAY)
                appendln()

                command.usages.forEach { it ->
                    append("    /", Color(0, 80, 200))
                    append(fullName, Color(0, 123, 255))
                    append(" ")

                    it.arguments.joint(" ".component()) {
                        if (it is LiteralArgument) {
                            it.name.component(Color.ORANGE)
                        } else {
                            "<".component(Color.GRAY)
                                .append(it.name.component(Color.ORANGE))
                                .append(">".component(Color.GRAY))
                        }
                    }.forEach {
                        +it
                    }

                    if (command.description != null) append(" - ", Color.GRAY).append(
                        command.description ?: return@forEach
                    )

                    appendln()
                }
            }

            if (command.examples.isNotEmpty()) {
                appendln()
                append("Examples", Color.RED)
                append(":", Color.GRAY)
                appendln()

                command.examples.forEach {
                    append("    /", Color(0, 80, 200))
                    append(fullName, Color(0, 123, 255))
                    append(
                        it.replaceFirst("^/".toRegex(), "")
                            .replaceFirst("^${fullName}".toRegex(), "")
                            .replaceFirst("^${command.name}".toRegex(), "")
                    )
                    appendln()
                }
            }

            appendln("--------------------------------------------------", Color.DARK_GRAY)
        }
    }
}

/**
 * A wrapper for a function that is called when Usage is executed
 * You can use SAM conversation in Java
 */
fun interface ContextAction<T> {
    /**
     * An method which replacing kotlin apply block.
     */
    fun CommandContext<T>.execute()
}