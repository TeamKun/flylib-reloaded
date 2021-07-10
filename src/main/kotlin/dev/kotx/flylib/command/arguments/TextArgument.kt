/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command.arguments

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import dev.kotx.flylib.command.Argument
import dev.kotx.flylib.command.SuggestionAction
import net.minecraft.server.v1_16_R3.CommandListenerWrapper

/**
 * Text argument.
 */
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

    /**
     * The type of text.
     */
    enum class Type {
        /**
         * A person word that does not put a space in between.
         * ## Example
         * `hello` `hogehoge`
         */
        WORD,

        /**
         * Sentences and words that contain spaces enclosed in double quotes.
         * ## Example
         * `hello` `hogehoge` `"some long text."`
         */
        PHRASE_QUOTED,

        /**
         * All text.
         * ## Example
         * `hello` `hoge` `Some long text.` `"Quoted long text."`
         */
        PHRASE
    }
}