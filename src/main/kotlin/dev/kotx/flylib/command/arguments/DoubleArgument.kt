/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command.arguments

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.context.CommandContext
import dev.kotx.flylib.command.Argument
import dev.kotx.flylib.command.ContextAction
import dev.kotx.flylib.command.SuggestionAction
import net.minecraft.server.v1_16_R3.CommandListenerWrapper

/**
 *  Double precision floating point argument. You can set the minimum and maximum values.
 *  If it falls below the minimum value or exceeds the maximum value, an error will be displayed on the client side, and even if you try to execute it, it will not be accepted.
 *  Expected input: `2` `6.3` `-12` `0` `0.0`
 *
 *  @param name Name of argument.
 *  @param suggestion Lambda expression for tab completion of its arguments.
 *  @param min The lowest possible value for this argument.
 *  @param max The highest possible value for this argument.
 *
 *  Check the following for the specifications of other arguments.
 *  @see Argument
 */
class DoubleArgument<T>(
    override val name: String,
    min: Double = Double.MIN_VALUE,
    max: Double = Double.MAX_VALUE,
    override val suggestion: SuggestionAction<T>? = null,
    override val action: ContextAction<T>? = null
) : Argument<Double, T> {
    override val type: ArgumentType<*>? = DoubleArgumentType.doubleArg(min, max)
    override fun parse(context: CommandContext<CommandListenerWrapper>, key: String): Double =
        DoubleArgumentType.getDouble(context, key)
}