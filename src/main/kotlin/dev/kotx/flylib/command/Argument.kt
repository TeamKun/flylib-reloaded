/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.v1_16_R3.CommandListenerWrapper

/**
 *  Command arguments. It can be used as a child element of Usage.
 *  There is a type in the argument, type analysis is automatically performed when the client inputs, and if it cannot be parsed, the command execution is refused.
 */
interface Argument<T, C> {
    /**
     * Name of argument.
     */
    val name: String

    /**
     * Mojang Brigadier argument type.
     */
    val type: ArgumentType<*>?

    /**
     * Lambda expression for tab completion of its arguments.
     */
    val suggestion: SuggestionAction<C>?

    /**
     * Argument context action
     */
    val action: ContextAction<C>?

    fun parse(context: CommandContext<CommandListenerWrapper>, key: String): T
}