/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command

import dev.kotx.flylib.util.ComponentBuilder
import dev.kotx.flylib.util.ComponentBuilderAction
import net.kyori.adventure.text.Component
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
     * Send message to command sender.
     */
    fun send(text: String) {
        sender.sendMessage(text)
    }

    /**
     * send message to command sender
     */
    fun send(component: Component) {
        sender.sendMessage(component)
    }

    /**
     * send message to command sender with ComponentBuilder
     */
    fun send(builder: ComponentBuilderAction) {
        sender.sendMessage(ComponentBuilder().apply { builder.apply { initialize() } }.build())
    }

    /**
     * send green colored message.
     */
    fun success(text: String) {
        send { append(text, Color.GREEN) }
    }

    /**
     * send yellow colored message.
     */
    fun warn(text: String) {
        send { append(text, Color.YELLOW) }
    }

    /**
     * send red colored message.
     */
    fun fail(text: String) {
        send { append(text, Color.RED) }
    }

    /**
     * send message with plugin name.
     */
    fun pluginSend(text: String) {
        send {
            append("[", Color.LIGHT_GRAY)
            append(plugin.name, Color.ORANGE)
            append("]", Color.LIGHT_GRAY)
            append(" ")
            append(text)
        }
    }

    /**
     * send message with plugin name.
     */
    fun pluginSend(component: Component) {
        send {
            append("[", Color.LIGHT_GRAY)
            append(plugin.name, Color.ORANGE)
            append("]", Color.LIGHT_GRAY)
            append(" ")
            append(component)
        }
    }

    /**
     * send message with plugin name using ComponentBuilder
     */
    fun pluginSend(builder: ComponentBuilderAction) {
        send {
            append("[", Color.LIGHT_GRAY)
            append(plugin.name, Color.ORANGE)
            append("]", Color.LIGHT_GRAY)
            append(" ")
            append(ComponentBuilder().apply { builder.apply { initialize() } }.build())
        }
    }

    /**
     * send green colored message with plugin name.
     */
    fun pluginSendSuccess(text: String) {
        send {
            append("[", Color.LIGHT_GRAY)
            append(plugin.name, Color.ORANGE)
            append("]", Color.LIGHT_GRAY)
            append(" ")
            append(text, Color.GREEN)
        }
    }

    /**
     * send yellow colored message with plugin name.
     */
    fun pluginSendWarn(text: String) {
        send {
            append("[", Color.LIGHT_GRAY)
            append(plugin.name, Color.ORANGE)
            append("]", Color.LIGHT_GRAY)
            append(" ")
            append(text, Color.YELLOW)
        }
    }

    /**
     * send red colored message with plugin name.
     */
    fun pluginSendError(text: String) {
        send {
            append("[", Color.LIGHT_GRAY)
            append(plugin.name, Color.ORANGE)
            append("]", Color.LIGHT_GRAY)
            append(" ")
            append(text, Color.RED)
        }
    }
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