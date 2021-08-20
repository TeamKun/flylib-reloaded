/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
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
class SuggestionBuilder(
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

    val player = sender as? Player
    val args = message.replaceFirst("^/".toRegex(), "").split(" ").drop(depth)

    /**
     * Suggest content.
     */
    @JvmOverloads
    fun suggest(content: String, tooltip: String? = null): SuggestionBuilder {
        suggestions.add(Suggestion(content, tooltip))
        return this
    }

    internal fun build() = suggestions.toList()
}

/**
 * Builder Action to add a suggestion
 */
fun interface SuggestionAction {
    /**
     * An method which replacing kotlin apply block.
     */
    fun SuggestionBuilder.initialize()
}