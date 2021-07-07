/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command.arguments

import com.mojang.brigadier.arguments.*
import com.mojang.brigadier.context.*
import dev.kotx.flylib.command.*
import net.minecraft.server.v1_16_R3.*

class LongArgument(
    override val name: String,
    min: Long = Long.MIN_VALUE,
    max: Long = Long.MAX_VALUE,
    override val suggestion: SuggestionBuilder.() -> Unit = {}
) : Argument<Long> {
    override val type: ArgumentType<*>? = LongArgumentType.longArg(min, max)

    override fun parse(context: CommandContext<CommandListenerWrapper>, key: String) = LongArgumentType.getLong(context, key)
}