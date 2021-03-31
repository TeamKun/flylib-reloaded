/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib

import kotx.minecraft.libs.flylib.command.Command
import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.command.CommandSender

operator fun List<Command>.get(query: String) =
    find { it.name.equals(query, true) } ?: find { it.aliases.any { it == query } }

fun CommandSender.send(block: ComponentBuilder.() -> Unit) {
    spigot().sendMessage(*ComponentBuilder().apply(block).create())
}