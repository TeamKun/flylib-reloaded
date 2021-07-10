/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command.arguments

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import dev.kotx.flylib.command.Argument
import dev.kotx.flylib.command.SuggestionAction
import net.minecraft.server.v1_16_R3.CommandListenerWrapper

class IntegerArgument(
        override val name: String,
        min: Int = Int.MIN_VALUE,
        max: Int = Int.MAX_VALUE,
        override val suggestion: SuggestionAction? = null
) : Argument<Int> {
    override val type: ArgumentType<*>? = IntegerArgumentType.integer(min, max)

    override fun parse(context: CommandContext<CommandListenerWrapper>, key: String): Int = IntegerArgumentType.getInteger(context, key)
}