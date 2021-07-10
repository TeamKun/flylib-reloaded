/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command

import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class CommandContext(
    val plugin: JavaPlugin,
    val command: Command,
    val sender: CommandSender,
    val world: World?,
    val message: String,
    depth: Int
) {
    val player = sender as? Player
    val args = message.replaceFirst("^/".toRegex(), "").split(" ").drop(depth)
}

fun interface ContextAction {
    fun CommandContext.execute()
}