/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.utils

import net.kyori.adventure.audience.*
import net.kyori.adventure.text.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.*
import org.bukkit.plugin.java.*
import java.awt.*

fun Audience.send(block: TextComponentAction) {
    sendMessage(Component.text().run { block.apply { initialize() }; this }.build(), MessageType.CHAT)
}

fun ComponentBuilder<*, *>.append(text: String): ComponentBuilder<*, *> = append(Component.text(text))
fun ComponentBuilder<*, *>.append(
    text: String,
    color: Color = Color.WHITE,
    style: Style = Style.empty(),
    decoration: TextDecoration? = null
): ComponentBuilder<*, *> = append(Component.text(text).color(color).style(style).run {
    if (decoration != null)
        decorate(decoration)
    else
        this
})

fun ComponentBuilder<*, *>.append(text: String, color: Color): ComponentBuilder<*, *> = append(Component.text(text).color(color))
fun ComponentBuilder<*, *>.append(text: String, style: Style): ComponentBuilder<*, *> = append(Component.text(text).style(style))
fun ComponentBuilder<*, *>.append(text: String, decoration: TextDecoration): ComponentBuilder<*, *> = append(Component.text(text).decorate(decoration))

fun ComponentBuilder<*, *>.appendln(text: String): ComponentBuilder<*, *> = append("$text\n")
fun ComponentBuilder<*, *>.appendln(text: String, color: Color): ComponentBuilder<*, *> = append("$text\n", color)
fun ComponentBuilder<*, *>.appendln(text: String, style: Style): ComponentBuilder<*, *> = append("$text\n", style)
fun ComponentBuilder<*, *>.appendln(text: String, decoration: TextDecoration): ComponentBuilder<*, *> = append("$text\n", decoration)
fun ComponentBuilder<*, *>.appendln(
    text: String,
    color: Color = Color.WHITE,
    style: Style = Style.empty(),
    decoration: TextDecoration? = null
): ComponentBuilder<*, *> = append("$text\n", color, style, decoration)

fun ComponentBuilder<*, *>.appendln(): ComponentBuilder<*, *> = this@appendln.append("\n")

fun TextComponent.color(color: Color) = color(TextColor.color(color.red, color.green, color.blue))
fun TextComponent.append(text: String): TextComponent = append(Component.text(text))
fun TextComponent.append(text: String, color: Color): TextComponent = append(Component.text(text).color(color))
fun TextComponent.append(text: String, style: Style): TextComponent = append(Component.text(text).style(style))
fun TextComponent.append(text: String, decoration: TextDecoration): TextComponent = append(Component.text(text).decorate(decoration))
fun TextComponent.appendln(text: String): TextComponent = append("$text\n")
fun TextComponent.appendln(text: String, color: Color): TextComponent = append("$text\n", color)
fun TextComponent.appendln(text: String, style: Style): TextComponent = append("$text\n", style)
fun TextComponent.appendln(text: String, decoration: TextDecoration): TextComponent = append("$text\n", decoration)
fun TextComponent.appendln(): TextComponent = this@appendln.append("\n")

fun interface TextComponentAction {
    fun ComponentBuilder<*, *>.initialize()
}

fun Audience.sendPluginMessage(plugin: JavaPlugin, block: TextComponentAction) {
    send {
        append("[", Color.GRAY)
        append(plugin.name, Color.RED)
        append("] ", Color.GRAY)
        block.apply { initialize() }
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

fun String.asTextComponent(color: Color = Color.WHITE) = Component.text(this, TextColor.color(color.red, color.green, color.blue))
fun String.asTextComponent(style: Style) = Component.text(this, style)
fun String.asTextComponent(decoration: TextDecoration) = Component.text(this, Style.style(decoration))