/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command.arguments

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import dev.kotx.flylib.command.Argument
import dev.kotx.flylib.command.ContextAction
import dev.kotx.flylib.command.SuggestionAction
import net.minecraft.server.v1_16_R3.CommandListenerWrapper

/**
 *  String argument type. You can specify string type.
 *  If it is not the specified type, an error will be displayed on the client side and the attempt to execute will not be accepted.
 *  String Type: WORD -> `hello` `world`
 *  String Type: PHRASE_QUOTED -> `hello` `world` `"This is quoted string"`
 *  String Type: PHRASE -> `hello string` `word` `"Quoted string"`
 *
 *  @param name Name of argument.
 *  @param type Text type.
 *  @param suggestion Lambda expression for tab completion of its arguments.
 *
 *  Check the following for the specifications of other arguments.
 *  @see Argument
 */
class StringArgument(
    override val name: String,
    type: Type = Type.WORD,
    override val suggestion: SuggestionAction? = null,
    override val action: ContextAction? = null
) : Argument<String> {
    override val type: StringArgumentType = when (type) {
        Type.WORD -> StringArgumentType.word()
        Type.PHRASE_QUOTED -> StringArgumentType.string()
        Type.PHRASE -> StringArgumentType.greedyString()
    }

    override fun parse(context: CommandContext<CommandListenerWrapper>, key: String): String =
        StringArgumentType.getString(context, key)

    /**
     * The type of argument input.
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