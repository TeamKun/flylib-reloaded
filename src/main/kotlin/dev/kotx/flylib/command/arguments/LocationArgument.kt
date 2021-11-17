/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command.arguments

import com.mojang.brigadier.context.CommandContext
import dev.kotx.flylib.command.Argument
import dev.kotx.flylib.command.ContextAction
import dev.kotx.flylib.command.SuggestionAction
import net.minecraft.server.v1_16_R3.ArgumentPosition
import net.minecraft.server.v1_16_R3.CommandListenerWrapper
import org.bukkit.Location

/**
 *  An argument that takes coordinates as an input value. You can also use "~" and "^".
 *  If it falls below the minimum value or exceeds the maximum value, an error will be displayed on the client side, and even if you try to execute it, it will not be accepted.
 *  Expected input : `123 456 789` `~ ~10 ~` `111 ~5 ^10`
 *
 *  @param name Name of argument.
 *  @param suggestion Lambda expression for tab completion of its arguments.
 *
 *  Check the following for the specifications of other arguments.
 *
 *  @see Argument
 */
class LocationArgument(
    override val name: String,
    override val suggestion: SuggestionAction? = null,
    override val action: ContextAction? = null
) : Argument<Location> {
    override val type: ArgumentPosition = ArgumentPosition.a()

    override fun parse(context: CommandContext<CommandListenerWrapper>, key: String): Location {
        val blockPosition = ArgumentPosition.a(context, key)
        return Location(
            null,
            blockPosition.x.toDouble(),
            blockPosition.y.toDouble(),
            blockPosition.z.toDouble(),
        )
    }
}