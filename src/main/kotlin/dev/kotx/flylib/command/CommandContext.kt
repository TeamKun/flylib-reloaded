/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command

import org.bukkit.*
import org.bukkit.command.*
import org.bukkit.entity.*
import org.bukkit.plugin.java.*

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