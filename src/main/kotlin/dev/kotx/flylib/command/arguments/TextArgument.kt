/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command.arguments

import com.mojang.brigadier.arguments.*
import com.mojang.brigadier.context.*
import dev.kotx.flylib.command.*
import net.minecraft.server.v1_16_R3.*

internal class TextArgument(name: String, type: TextArgumentType = TextArgumentType.WORD, suggestion: SuggestionBuilder.() -> Unit = {}) : Argument<String>(name, suggestion) {
    override val type = when (type) {
        TextArgumentType.WORD -> StringArgumentType.word()
        TextArgumentType.PHRASE_QUOTED -> StringArgumentType.string()
        TextArgumentType.PHRASE -> StringArgumentType.greedyString()
    }

    override fun parse(context: CommandContext<CommandListenerWrapper>, key: String): String = StringArgumentType.getString(context, key)
}