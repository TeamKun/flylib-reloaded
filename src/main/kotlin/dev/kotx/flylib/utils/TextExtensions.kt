/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.utils

import net.kyori.adventure.audience.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.*
import org.bukkit.command.*
import org.bukkit.plugin.java.*
import java.awt.*

fun CommandSender.send(block: TextComponent.Builder.() -> Unit) {
    sendMessage(Component.text().apply(block).build(), MessageType.CHAT)
}

fun TextComponent.Builder.append(text: String): TextComponent.Builder = append(Component.text(text))
fun TextComponent.Builder.append(
    text: String,
    color: Color = Color.WHITE,
    style: Style = Style.empty(),
    decoration: TextDecoration? = null
): TextComponent.Builder = append(Component.text(text).color(color).style(style).run {
    if (decoration != null)
        decorate(decoration)
    else
        this
})

fun TextComponent.Builder.append(text: String, color: Color): TextComponent.Builder = append(Component.text(text).color(color))
fun TextComponent.Builder.append(text: String, style: Style): TextComponent.Builder = append(Component.text(text).style(style))
fun TextComponent.Builder.append(text: String, decoration: TextDecoration): TextComponent.Builder = append(Component.text(text).decorate(decoration))

fun TextComponent.Builder.appendln(text: String): TextComponent.Builder = append("$text\n")
fun TextComponent.Builder.appendln(text: String, color: Color): TextComponent.Builder = append("$text\n", color)
fun TextComponent.Builder.appendln(text: String, style: Style): TextComponent.Builder = append("$text\n", style)
fun TextComponent.Builder.appendln(text: String, decoration: TextDecoration): TextComponent.Builder = append("$text\n", decoration)
fun TextComponent.Builder.appendln(
    text: String,
    color: Color = Color.WHITE,
    style: Style = Style.empty(),
    decoration: TextDecoration? = null
): TextComponent.Builder = append("$text\n", color, style, decoration)

fun TextComponent.Builder.appendln(): TextComponent.Builder = this@appendln.append("\n")

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
    fun TextComponent.Builder.initialize()
}

fun CommandSender.sendPluginMessage(plugin: JavaPlugin, block: TextComponentAction) {
    send {
        append("[", Color.GRAY)
        append(plugin.name, Color.RED)
        append("] ", Color.GRAY)
        block.apply { initialize() }
    }
}

fun CommandSender.sendPluginMessage(plugin: JavaPlugin, text: String) {
    sendPluginMessage(plugin) {
        append(text, Color.WHITE)
    }
}

fun CommandSender.fail(plugin: JavaPlugin, text: String) {
    sendPluginMessage(plugin) {
        append(text, Color.RED)
    }
}

fun CommandSender.warn(plugin: JavaPlugin, text: String) {
    sendPluginMessage(plugin) {
        append(text, Color.YELLOW)
    }
}

fun CommandSender.success(plugin: JavaPlugin, text: String) {
    sendPluginMessage(plugin) {
        append(text, Color.GREEN)
    }
}

fun CommandSender.fail(text: String) {
    send {
        append(text, Color.RED)
    }
}

fun CommandSender.warn(text: String) {
    send {
        append(text, Color.YELLOW)
    }
}

fun CommandSender.success(text: String) {
    send {
        append(text, Color.GREEN)
    }
}

fun String.asTextComponent(color: Color = Color.WHITE) = Component.text(this, TextColor.color(color.red, color.green, color.blue))
fun String.asTextComponent(style: Style) = Component.text(this, style)
fun String.asTextComponent(decoration: TextDecoration) = Component.text(this, Style.style(decoration))