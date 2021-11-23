/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command.arguments

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.context.CommandContext
import dev.kotx.flylib.command.Argument
import dev.kotx.flylib.command.ContextAction
import dev.kotx.flylib.command.SuggestionAction
import net.minecraft.server.v1_16_R3.CommandListenerWrapper

/**
 *  Anti-precision floating point argument. You can set the minimum and maximum values.
 *  If it falls below the minimum value or exceeds the maximum value, an error will be displayed on the client side, and even if you try to execute it, it will not be accepted.
 *  Expected input: `2` `6.3` `-12` `0` `0.0`
 *
 *  @param name Name of argument.
 *  @param min The lowest possible value for this argument.
 *  @param max The highest possible value for this argument.
 *  @param suggestion Lambda expression for tab completion of its arguments.
 *
 *  Check the following for the specifications of other arguments.
 *  @see Argument
 */
class FloatArgument<T>(
    override val name: String,
    min: Float = Float.MIN_VALUE,
    max: Float = Float.MAX_VALUE,
    override val suggestion: SuggestionAction<T>? = null,
    override val action: ContextAction<T>? = null
) : Argument<Float, T> {
    override val type: ArgumentType<*>? = FloatArgumentType.floatArg(min, max)
    override fun parse(context: CommandContext<CommandListenerWrapper>, key: String): Float =
        FloatArgumentType.getFloat(context, key)
}