/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.v1_16_R3.CommandListenerWrapper

/**
 *  Command arguments. It can be used as a child element of Usage.
 *  There is a type in the argument, type analysis is automatically performed when the client inputs, and if it cannot be parsed, the command execution is refused.
 *
 *  @param name Name of argument.
 *  @param suggestion Lambda expression for tab completion of its arguments.
 *  @param type Mojang Brigadier argument type.
 */
interface Argument<T> {
    val name: String
    val type: ArgumentType<*>?
    val suggestion: SuggestionAction?
    val action: ContextAction?

    fun parse(context: CommandContext<CommandListenerWrapper>, key: String): T
}