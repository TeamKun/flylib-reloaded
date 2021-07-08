/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command

import com.mojang.brigadier.arguments.*
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.v1_16_R3.*

interface Argument<T> {
    val name: String
    val suggestion: (SuggestionBuilder.() -> Unit)?
    val type: ArgumentType<*>?

    fun parse(context: CommandContext<CommandListenerWrapper>, key: String): T
}