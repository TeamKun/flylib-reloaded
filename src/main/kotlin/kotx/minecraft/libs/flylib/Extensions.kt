/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib

import kotx.minecraft.libs.flylib.command.Command
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.awt.Color

operator fun List<Command>.get(query: String) =
    find { it.name.equals(query, true) } ?: find { it.aliases.any { it == query } }

fun CommandSender.send(block: TextComponent.Builder.() -> Unit) {
    sendMessage(Component.text().apply(block).build(), MessageType.CHAT)
}

fun TextComponent.Builder.append(text: String): TextComponent.Builder = append(Component.text(text))
fun TextComponent.Builder.append(text: String, color: Color): TextComponent.Builder = append(Component.text(text).color(color))
fun TextComponent.Builder.append(text: String, style: Style): TextComponent.Builder = append(Component.text(text).style(style))
fun TextComponent.Builder.append(text: String, decoration: TextDecoration): TextComponent.Builder =
    append(Component.text(text).decorate(decoration))

fun TextComponent.Builder.appendln(text: String): TextComponent.Builder = append("$text\n")
fun TextComponent.Builder.appendln(text: String, color: Color): TextComponent.Builder = append("$text\n", color)
fun TextComponent.Builder.appendln(text: String, style: Style): TextComponent.Builder = append("$text\n", style)
fun TextComponent.Builder.appendln(text: String, decoration: TextDecoration): TextComponent.Builder = append("$text\n", decoration)

fun TextComponent.Builder.appendln(): TextComponent.Builder = this@appendln.append("\n")

fun TextComponent.color(color: Color) = color(TextColor.color(color.red, color.green, color.blue))


fun CommandSender.sendPluginMessage(plugin: JavaPlugin, block: TextComponent.Builder.() -> Unit) {
    send {
        append("[", Color.GRAY)
        append(plugin.name, Color.RED)
        append("] ", Color.GRAY)
        apply(block)
    }
}

fun CommandSender.sendPluginMessage(plugin: JavaPlugin, text: String) {
    sendPluginMessage(plugin) {
        append(text, Color.WHITE)
    }
}

fun CommandSender.sendErrorMessage(plugin: JavaPlugin, text: String) {
    sendPluginMessage(plugin) {
        append(text, Color.RED)
    }
}

fun CommandSender.sendWarnMessage(plugin: JavaPlugin, text: String) {
    sendPluginMessage(plugin) {
        append(text, Color.YELLOW)
    }
}

fun CommandSender.sendSuccessMessage(plugin: JavaPlugin, text: String) {
    sendPluginMessage(plugin) {
        append(text, Color.GREEN)
    }
}

fun String.asTextComponent(color: Color = Color.WHITE) = Component.text(this, TextColor.color(color.red, color.green, color.blue))
fun String.asTextComponent(style: Style) = Component.text(this, style)
fun String.asTextComponent(decoration: TextDecoration) = Component.text(this, Style.style(decoration))