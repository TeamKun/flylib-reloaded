/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command.arguments

import com.mojang.brigadier.context.CommandContext
import dev.kotx.flylib.command.Argument
import dev.kotx.flylib.command.SuggestionAction
import net.minecraft.server.v1_16_R3.ArgumentPosition
import net.minecraft.server.v1_16_R3.CommandListenerWrapper
import org.bukkit.Location

/**
 * An argument that represents a location.
 */
class LocationArgument(
    override val name: String,
    override val suggestion: SuggestionAction? = null
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