/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command.arguments

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.context.CommandContext
import dev.kotx.flylib.command.Argument
import dev.kotx.flylib.command.SuggestionAction
import net.minecraft.server.v1_16_R3.CommandListenerWrapper

/**
 *  Long value argument. You can specify the maximum and minimum values.
 *  If you enter it in the wrong coordinate format, an error will be displayed on the client side and even if you try to execute it, it will not be accepted.
 *  Expected input: `2` `-12` `0`
 *
 *  @param name Name of argument.
 *  @param suggestion Lambda expression for tab completion of its arguments.
 *  @param min The lowest possible value for this argument.
 *  @param max The highest possible value for this argument.
 *
 *  Check the following for the specifications of other arguments.
 *
 *  @see Argument
 */
class LongArgument(
    override val name: String,
    min: Long = Long.MIN_VALUE,
    max: Long = Long.MAX_VALUE,
    override val suggestion: SuggestionAction? = null
) : Argument<Long> {
    override val type: ArgumentType<*>? = LongArgumentType.longArg(min, max)

    override fun parse(context: CommandContext<CommandListenerWrapper>, key: String) =
        LongArgumentType.getLong(context, key)
}