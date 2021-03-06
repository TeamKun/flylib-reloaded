/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command.arguments

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import dev.kotx.flylib.command.Argument
import dev.kotx.flylib.command.ContextAction
import dev.kotx.flylib.command.SuggestionAction
import net.minecraft.server.v1_16_R3.CommandListenerWrapper

/**
 *  An argument that does not have any user input. Input other than "name" is not allowed.
 *  If anything other than "name" is entered, an error will be displayed on the client side and even if you try to execute it, it will not be accepted.
 *  Expected input (if "name" specified "test"): `test`
 *
 *  @param name Name of argument. This can be taken as an argument.
 *
 *  Check the following for the specifications of other arguments.
 *  @see Argument
 */
class LiteralArgument<T>(
    override val name: String,
    override val action: ContextAction<T>? = null
) : Argument<String, T> {
    override val type: ArgumentType<*>? = null
    override val suggestion: SuggestionAction<T>? = null

    override fun parse(context: CommandContext<CommandListenerWrapper>, key: String) = key
}