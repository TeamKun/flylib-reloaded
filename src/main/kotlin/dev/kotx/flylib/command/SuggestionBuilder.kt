/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command

import org.bukkit.Server
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * A builder that creates a list of suggestions.
 */
class SuggestionBuilder<T>(
    /**
     * Your plugin.
     */
    val plugin: JavaPlugin,
    /**
     * Executed command.
     */
    val command: Command<T>,
    /**
     * Executed command sender. (It doesn't matter if you are a player or not.)
     */
    val sender: CommandSender,
    /**
     * The world where the command was executed
     */
    val world: World?,
    /**
     * The server on which the plugin was run
     */
    val server: Server,
    /**
     * Command input message.
     */
    val message: String,
    depth: Int,
    val typedArgs: List<Any?>
) {
    private val suggestions = mutableListOf<Suggestion>()

    /**
     * The player who is about to execute the command.
     */
    val player = sender as? Player

    /**
     * Command arguments. The remaining arguments except the beginning of the command are assigned.
     * These are all returned as strings.
     * If you need parsed values, see typedArgs.
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
     *
     * @see typedArgs
     */
    val args = message.replaceFirst("^/".toRegex(), "").split(" ").drop(depth)

    /**
     * Suggest content.
     *
     * Tooltips: The string displayed when the mouse hovers over a suggestion.
     */
    @JvmOverloads
    fun suggest(content: String, tooltip: String? = null): SuggestionBuilder<T> {
        suggestions.add(Suggestion(content, tooltip))
        return this
    }

    /**
     * Suggest multiple content.
     */
    fun suggestAll(contents: List<String>): SuggestionBuilder<T> {
        suggestions.addAll(contents.map { Suggestion(it, null) })
        return this
    }

    internal fun build() = suggestions.toList()
}

/**
 * Builder Action to add a suggestion
 */
fun interface SuggestionAction<T> {
    /**
     * An method which replacing kotlin apply block.
     */
    fun SuggestionBuilder<T>.initialize()
}