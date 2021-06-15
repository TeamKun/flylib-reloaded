/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command.internal

import dev.kotx.flylib.command.Command
import org.bukkit.*
import org.bukkit.command.*
import org.bukkit.entity.*
import org.bukkit.plugin.java.*

class SuggestionBuilder (
    val command: Command,
    val plugin: JavaPlugin,
    val sender: CommandSender,
    val message: String,
    val args: Array<String>,
    val typedArgs: Array<Any?>
) {
    val player: Player? = sender as? Player
    val server: Server? = player?.server
    val world: World? = player?.world

    private val suggestions = mutableListOf<Suggestion>()

    @JvmOverloads
    fun suggest(
        content: String,
        tooltip: String? = null
    ): SuggestionBuilder {
        suggestions.add(Suggestion(content, tooltip))
        return this
    }

    fun build() = suggestions.toList()
}