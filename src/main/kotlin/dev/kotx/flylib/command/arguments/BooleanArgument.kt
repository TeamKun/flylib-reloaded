package dev.kotx.flylib.command.arguments

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.context.CommandContext
import dev.kotx.flylib.command.Argument
import dev.kotx.flylib.command.SuggestionAction
import net.minecraft.server.v1_16_R3.CommandListenerWrapper

/**
 *  Boolean argument. It must take true or false.
 *  If the input value is neither true nor false (not a valid expression of a boolean), an error will be displayed on the client side, and even if you try to execute it, it will not be accepted.
 *  Expected input: `true` `false` `TRUE` 'False'
 *
 *  Check the following for the specifications of other arguments.
 *  @see Argument
 */
class BooleanArgument(
    override val name: String,
    override val suggestion: SuggestionAction? = null
) : Argument<Boolean> {
    override val type: ArgumentType<*>? = BoolArgumentType.bool()
    override fun parse(context: CommandContext<CommandListenerWrapper>, key: String): Boolean =
        BoolArgumentType.getBool(context, key)
}