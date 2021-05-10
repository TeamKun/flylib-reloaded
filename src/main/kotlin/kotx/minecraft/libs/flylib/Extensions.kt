/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib

import com.mojang.brigadier.context.CommandContext
import kotx.minecraft.libs.flylib.command.Command
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minecraft.server.v1_16_R3.CommandListenerWrapper
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.awt.Color

operator fun List<Command>.get(query: String) =
    find { it.name.equals(query, true) } ?: find { it.aliases.any { it == query } }

fun CommandContext<CommandListenerWrapper>.asFlyLibContext(command: Command, depth: Int = 0): kotx.minecraft.libs.flylib.command.CommandContext =
    kotx.minecraft.libs.flylib.command.CommandContext(
        command,
        command.plugin,
        source.bukkitSender,
        source.bukkitSender as? Player,
        source.bukkitSender.server,
        input.replaceFirst("/", ""),
        input.replaceFirst("/", "").split(" ").drop(1 + depth).toTypedArray()
    )

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

fun <T> List<T>.joint(other: T): List<T> {
    val res = mutableListOf<T>()
    forEachIndexed { i, it ->
        res.add(it)
        if (i < size - 1)
            res.add(other)
    }

    return res.toList()
}

fun <T, E> List<T>.joint(joiner: E, target: (T) -> E) {
    map(target).joint(joiner)
}

@JvmName("jointT")
fun <T> List<T>.joint(joiner: T, action: (T) -> Unit) {
    joint(joiner).forEach(action)
}

fun <T> List<T>.joint(other: (T) -> T): List<T> {
    val res = mutableListOf<T>()
    forEachIndexed { i, it ->
        res.add(it)
        if (i < size - 1)
            res.add(other(it))
    }

    return res.toList()
}