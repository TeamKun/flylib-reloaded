/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command.arguments

import com.mojang.brigadier.arguments.*
import com.mojang.brigadier.context.CommandContext
import dev.kotx.flylib.command.*
import net.minecraft.server.v1_16_R3.*

class TextArgument(
    override val name: String,
    type: Type = Type.WORD,
    override val suggestion: SuggestionAction? = null
) : Argument<String> {
    override val type = when (type) {
        Type.WORD -> StringArgumentType.word()
        Type.PHRASE_QUOTED -> StringArgumentType.string()
        Type.PHRASE -> StringArgumentType.greedyString()
    }

    override fun parse(context: CommandContext<CommandListenerWrapper>, key: String) = StringArgumentType.getString(context, key)

    enum class Type {
        WORD,
        PHRASE_QUOTED,
        PHRASE
    }
}