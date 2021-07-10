/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command.arguments

import com.mojang.brigadier.context.CommandContext
import dev.kotx.flylib.command.*
import net.minecraft.server.v1_16_R3.*
import org.bukkit.util.*

class VectorArgument(
    override val name: String,
    override val suggestion: SuggestionAction? = null,
) : Argument<Vector> {
    override val type = ArgumentVec3.a()
    override fun parse(context: CommandContext<CommandListenerWrapper>, key: String): Vector {
        val vec = ArgumentVec3.a(context, key)
        return Vector(
            vec.x,
            vec.y,
            vec.z
        )
    }
}