/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.util

import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.awt.Color

/**
 * send string message
 */
fun CommandSender.message(text: String) = sendMessage(text)

/**
 * send string colored message
 */
fun CommandSender.message(text: String, color: Color) = message { append(text, color) }

/**
 * send component
 */
fun CommandSender.message(component: Component) = sendMessage(component)

/**
 * send component via builder
 */
fun CommandSender.message(builder: ComponentBuilderAction) =
    message(ComponentBuilder().apply { builder.apply { initialize() } }.build())

/**
 * send green string
 */
fun CommandSender.success(text: String) = message { append(text, Color.GREEN) }

/**
 * send yellow string
 */
fun CommandSender.warn(text: String) = message { append(text, Color.YELLOW) }

/**
 * send red string
 */
fun CommandSender.fail(text: String) = message { append(text, Color.RED) }

/**
 * send string with plugin name
 */
fun CommandSender.pluginMessage(plugin: JavaPlugin, text: String) = pluginMessage(plugin, text, Color.WHITE)

/**
 * send colored string with plugin name
 */
fun CommandSender.pluginMessage(plugin: JavaPlugin, text: String, color: Color) = message {
    append("[", Color.LIGHT_GRAY)
    append(plugin.name, Color.ORANGE)
    append("]", Color.LIGHT_GRAY)
    append(" ")
    append(text, color)
}

/**
 * send component with plugin name
 */
fun CommandSender.pluginMessage(plugin: JavaPlugin, component: Component) = message {
    append("[", Color.LIGHT_GRAY)
    append(plugin.name, Color.ORANGE)
    append("]", Color.LIGHT_GRAY)
    append(" ")
    append(component)
}

/**
 * send component via builder with plugin name
 */
fun CommandSender.pluginMessage(plugin: JavaPlugin, builder: ComponentBuilderAction) = message {
    append("[", Color.LIGHT_GRAY)
    append(plugin.name, Color.ORANGE)
    append("]", Color.LIGHT_GRAY)
    append(" ")
    apply { builder.apply { initialize() } }
}

/**
 * send green string with plugin name
 */
fun CommandSender.pluginMessageSuccess(plugin: JavaPlugin, text: String) = pluginMessage(plugin, text, Color.GREEN)

/**
 * send yellow string with plugin name
 */
fun CommandSender.pluginMessageWarn(plugin: JavaPlugin, text: String) = pluginMessage(plugin, text, Color.YELLOW)

/**
 * send red string with plugin name
 */
fun CommandSender.pluginMessageFail(plugin: JavaPlugin, text: String) = pluginMessage(plugin, text, Color.RED)