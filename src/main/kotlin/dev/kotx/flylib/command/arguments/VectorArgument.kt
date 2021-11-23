/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command.arguments

import com.mojang.brigadier.context.CommandContext
import dev.kotx.flylib.command.Argument
import dev.kotx.flylib.command.ContextAction
import dev.kotx.flylib.command.SuggestionAction
import net.minecraft.server.v1_16_R3.ArgumentVec3
import net.minecraft.server.v1_16_R3.CommandListenerWrapper
import org.bukkit.util.Vector

/**
 *  An argument that takes direction as an input value. You can also use "~" and "^".
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
class VectorArgument<T>(
    override val name: String,
    override val suggestion: SuggestionAction<T>? = null,
    override val action: ContextAction<T>? = null
) : Argument<Vector, T> {
    override val type: ArgumentVec3 = ArgumentVec3.a()
    override fun parse(context: CommandContext<CommandListenerWrapper>, key: String): Vector {
        val vec = ArgumentVec3.a(context, key)
        return Vector(
            vec.x,
            vec.y,
            vec.z
        )
    }
}