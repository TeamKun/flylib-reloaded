/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command.arguments

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import dev.kotx.flylib.command.Argument
import dev.kotx.flylib.command.SuggestionAction
import net.minecraft.server.v1_16_R3.CommandListenerWrapper

class LiteralArgument(override val name: String) : Argument<String> {
    override val type: ArgumentType<*>? = null
    override val suggestion: SuggestionAction? = null

    override fun parse(context: CommandContext<CommandListenerWrapper>, key: String) = key
}