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
import java.awt.Color

operator fun List<Command>.get(query: String) =
    find { it.name.equals(query, true) } ?: find { it.aliases.any { it == query } }

fun CommandSender.send(block: TextComponent.Builder.() -> Unit) {
    sendMessage(Component.text().apply(block).build(), MessageType.CHAT)
}

fun TextComponent.Builder.appendText(text: String): TextComponent.Builder = append(Component.text(text))
fun TextComponent.Builder.appendText(text: String, color: Color): TextComponent.Builder = append(Component.text(text).color(color))
fun TextComponent.Builder.appendText(text: String, style: Style): TextComponent.Builder = append(Component.text(text).style(style))
fun TextComponent.Builder.appendText(text: String, decoration: TextDecoration): TextComponent.Builder =
    append(Component.text(text).decorate(decoration))

fun TextComponent.color(color: Color) = color(TextColor.color(color.red, color.green, color.blue))