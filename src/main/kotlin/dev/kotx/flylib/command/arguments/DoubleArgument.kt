package dev.kotx.flylib.command.arguments

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.context.CommandContext
import dev.kotx.flylib.command.Argument
import dev.kotx.flylib.command.SuggestionAction
import net.minecraft.server.v1_16_R3.CommandListenerWrapper

/**
 *  Double precision floating point argument. You can set the minimum and maximum values.
 *  If it falls below the minimum value or exceeds the maximum value, an error will be displayed on the client side, and even if you try to execute it, it will not be accepted.
 *  Expected input: `2` `6.3` `-12` `0` `0.0`
 *
 *  Check the following for the specifications of other arguments.
 *  @see Argument
 */
class DoubleArgument(
    override val name: String,
    override val suggestion: SuggestionAction?,
    min: Double = Double.MIN_VALUE,
    max: Double = Double.MAX_VALUE,
) : Argument<Double> {
    override val type: ArgumentType<*>? = DoubleArgumentType.doubleArg(min, max)
    override fun parse(context: CommandContext<CommandListenerWrapper>, key: String): Double =
        DoubleArgumentType.getDouble(context, key)
}