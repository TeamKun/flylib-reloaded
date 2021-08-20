package dev.kotx.flylib.command.arguments

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.context.CommandContext
import dev.kotx.flylib.command.Argument
import dev.kotx.flylib.command.SuggestionAction
import net.minecraft.server.v1_16_R3.CommandListenerWrapper

class DoubleArgument(
    override val name: String,
    min: Double = Double.MIN_VALUE,
    max: Double = Double.MAX_VALUE,
    override val suggestion: SuggestionAction?,
) : Argument<Double> {
    override val type: ArgumentType<*>? = DoubleArgumentType.doubleArg(min, max)
    override fun parse(context: CommandContext<CommandListenerWrapper>, key: String): Double =
        DoubleArgumentType.getDouble(context, key)
}