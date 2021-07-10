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
        /**
         * Your plugin.
         */
        val plugin: JavaPlugin,
        /**
         * Executed command.
         */
        val command: Command,
        /**
         * Executed command sender. (It doesn't matter if you are a player or not.)
         */
        val sender: CommandSender,
        /**
         * The world where the command was executed
         */
        val world: World?,
        /**
         * Command input message.
         */
        val message: String,
        depth: Int
) {
    /**
     * Executed command sender. (Limited to the player, but null if executed by someone other than the player.)
     */
    val player = sender as? Player

    /**
     * Command arguments. The remaining arguments except the beginning of the command are assigned.
     *
     * ## Example
     * ### Basic command
     * /command <number>
     *     args: <number>
     *
     * ### Nested command
     * /parent children <arg1> <arg2>
     *     args: <arg1>, <arg2>
     *
     * ### Usage argument
     * /command <literal> <text> <number> <entity>
     *     args: <literal>, <text>, <number>, <entity>
     */
    val args = message.replaceFirst("^/".toRegex(), "").split(" ").drop(depth)
}

fun interface ContextAction {
    fun CommandContext.execute()
}