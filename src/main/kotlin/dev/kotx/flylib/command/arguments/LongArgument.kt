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
 * A long argument
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