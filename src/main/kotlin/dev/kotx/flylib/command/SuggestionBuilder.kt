/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command

import org.bukkit.command.CommandSender
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
         * The command you are trying to add a suggestion to.
         */
        val command: Command,
        /**
         * The target for which the proposal is displayed.
         */
        val sender: CommandSender,
        /**
         * The content as of the time it is currently entered.
         */
        val message: String,
        /**
         * Argument by input when the proposal is displayed.
         */
        val args: List<String>
) {
    private val suggestions = mutableListOf<Suggestion>()

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
    fun SuggestionBuilder.initialize()
}