/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.v1_16_R3.CommandListenerWrapper

/**
 * Usage argument.
 */
interface Argument<T> {
    val name: String
    val suggestion: SuggestionAction?
    val type: ArgumentType<*>?

    fun parse(context: CommandContext<CommandListenerWrapper>, key: String): T
}