/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */
@file:JvmName("ChatUtils")
@file:JvmMultifileClass

package dev.kotx.flylib.utils

import dev.kotx.flylib.utils.component.*
import net.kyori.adventure.audience.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.*
import org.bukkit.plugin.java.*
import java.awt.*

fun Component.content() = (this as TextComponent).content()

fun text(block: TextComponentAction) = TextComponentBuilder().apply { block.apply { initialize() } }.build()

fun Audience.send(text: String) {
    send {
        append(text)
    }
}

fun Audience.send(block: TextComponentAction) {
    sendMessage(TextComponentBuilder().apply { block.apply { initialize() } }.build(), MessageType.CHAT)
}

fun interface TextComponentAction {
    fun TextComponentBuilder.initialize()
}

fun Audience.sendPluginMessage(plugin: JavaPlugin, block: TextComponentAction) {
    send {
        append("[", Color.GRAY)
        append(plugin.name, Color.RED)
        append("] ", Color.GRAY)
        apply { block.apply { initialize() } }
    }
}

fun Audience.sendPluginMessage(plugin: JavaPlugin, text: String) {
    sendPluginMessage(plugin) {
        append(text, Color.WHITE)
    }
}

fun Audience.fail(plugin: JavaPlugin, text: String) {
    sendPluginMessage(plugin) {
        append(text, Color.RED)
    }
}

fun Audience.warn(plugin: JavaPlugin, text: String) {
    sendPluginMessage(plugin) {
        append(text, Color.YELLOW)
    }
}

fun Audience.success(plugin: JavaPlugin, text: String) {
    sendPluginMessage(plugin) {
        append(text, Color.GREEN)
    }
}

fun Audience.fail(text: String) {
    send {
        append(text, Color.RED)
    }
}

fun Audience.warn(text: String) {
    send {
        append(text, Color.YELLOW)
    }
}

fun Audience.success(text: String) {
    send {
        append(text, Color.GREEN)
    }
}

@JvmOverloads
fun String.component(color: Color = Color.WHITE, vararg decorations: TextDecoration = emptyArray(), style: Style.Builder.() -> Unit = {}) =
    Component.text(this, Style.style().color(TextColor.color(color.rgb)).decorate(*decorations).apply(style).build())

fun String.component(vararg decorations: TextDecoration = emptyArray(), style: Style.Builder.() -> Unit = {}) =
    Component.text(this, Style.style().decorate(*decorations).apply(style).build())

fun String.component(style: Style) =
    Component.text(this, style)