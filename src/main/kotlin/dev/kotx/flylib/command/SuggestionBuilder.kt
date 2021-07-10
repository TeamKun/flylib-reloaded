/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command

import org.bukkit.command.*
import org.bukkit.plugin.java.*

class SuggestionBuilder(
    val plugin: JavaPlugin,
    val command: Command,
    val sender: CommandSender,
    val message: String,
    val args: List<String>
) {
    private val suggestions = mutableListOf<Suggestion>()

    fun suggest(content: String, tooltip: String? = null): SuggestionBuilder {
        suggestions.add(Suggestion(content, tooltip))
        return this
    }

    internal fun build() = suggestions.toList()
}

fun interface SuggestionAction {
    fun SuggestionBuilder.initialize()
}