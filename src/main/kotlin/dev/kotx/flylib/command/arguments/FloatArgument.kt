package dev.kotx.flylib.command.arguments

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.context.CommandContext
import dev.kotx.flylib.command.Argument
import dev.kotx.flylib.command.SuggestionAction
import net.minecraft.server.v1_16_R3.CommandListenerWrapper

class FloatArgument(
    override val name: String,
    min: Float = Float.MIN_VALUE,
    max: Float = Float.MAX_VALUE,
    override val suggestion: SuggestionAction?,
) : Argument<Float> {
    override val type: ArgumentType<*>? = FloatArgumentType.floatArg(min, max)
    override fun parse(context: CommandContext<CommandListenerWrapper>, key: String): Float =
        FloatArgumentType.getFloat(context, key)
}