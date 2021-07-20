/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command

import dev.kotx.flylib.util.ComponentBuilder
import dev.kotx.flylib.util.ComponentBuilderAction
import dev.kotx.flylib.util.message
import dev.kotx.flylib.util.pluginMessage
import net.kyori.adventure.text.Component
import org.bukkit.Server
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.awt.Color

/**
 * Command context.
 * It is given as an argument when the command is executed and when Usage is executed.
 * In Kotlin it is given as a receiver.
 */
class CommandContext(
    /**
     * Your plugin.
     */
    val plugin: JavaPlugin,
    /**
     * Executed command.
     */
    val command: Command,
    /**
     * Executed command sender. (It doesn't matter if you are a player or not.)
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
     * Command input message.
     */
    val message: String,
    depth: Int
) {
    /**
     * Executed command sender. (Limited to the player, but null if executed by someone other than the player.)
     */
    val player = sender as? Player

    /**
     * Command arguments. The remaining arguments except the beginning of the command are assigned.
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
     */
    val args = message.replaceFirst("^/".toRegex(), "").split(" ").drop(depth)

    /**
     * send string message
     */
    fun message(text: String) = sender.sendMessage(text)

    /**
     * send string colored message
     */
    fun message(text: String, color: Color) = sender.message { append(text, color) }

    /**
     * send component
     */
    fun message(component: Component) = sender.sendMessage(component)

    /**
     * send component via builder
     */
    fun message(builder: ComponentBuilderAction) =
        sender.sendMessage(ComponentBuilder().apply { builder.apply { initialize() } }.build())

    /**
     * send green string
     */
    fun success(text: String) = sender.message { append(text, Color.GREEN) }

    /**
     * send yellow string
     */
    fun warn(text: String) = sender.message { append(text, Color.YELLOW) }

    /**
     * send red string
     */
    fun fail(text: String) = sender.message { append(text, Color.RED) }

    /**
     * send string with plugin name
     */
    fun pluginMessage(text: String) = sender.pluginMessage(plugin, text, Color.WHITE)

    /**
     * send colored string with plugin name
     */
    fun pluginMessage(text: String, color: Color) = sender.message {
        append("[", Color.LIGHT_GRAY)
        append(plugin.name, Color.ORANGE)
        append("]", Color.LIGHT_GRAY)
        append(" ")
        append(text, color)
    }

    /**
     * send component with plugin name
     */
    fun pluginMessage(component: Component) = sender.message {
        append("[", Color.LIGHT_GRAY)
        append(plugin.name, Color.ORANGE)
        append("]", Color.LIGHT_GRAY)
        append(" ")
        append(component)
    }

    /**
     * send component via builder with plugin name
     */
    fun pluginMessage(builder: ComponentBuilderAction) = sender.message {
        append("[", Color.LIGHT_GRAY)
        append(plugin.name, Color.ORANGE)
        append("]", Color.LIGHT_GRAY)
        append(" ")
        append(ComponentBuilder().apply { builder.apply { initialize() } }.build())
    }

    /**
     * send green string with plugin name
     */
    fun pluginMessageSuccess(text: String) = sender.pluginMessage(plugin, text, Color.GREEN)

    /**
     * send yellow string with plugin name
     */
    fun pluginMessageWarn(text: String) = sender.pluginMessage(plugin, text, Color.YELLOW)

    /**
     * send red string with plugin name
     */
    fun pluginMessageFail(text: String) = sender.pluginMessage(plugin, text, Color.RED)
}

/**
 * A wrapper for a function that is called when Usage is executed
 * You can use SAM conversation in Java
 */
fun interface ContextAction {
    /**
     * An method which replacing kotlin apply block.
     */
    fun CommandContext.execute()
}