/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command

import com.mojang.brigadier.arguments.*
import com.mojang.brigadier.context.*
import net.minecraft.server.v1_16_R3.*

internal open class Argument<T>(
    internal val name: String,
    internal val suggestion: SuggestionBuilder.() -> Unit
) {
    internal open val type: ArgumentType<*>? = null

    internal open fun parse(context: CommandContext<CommandListenerWrapper>, key: String): T = key as T
}