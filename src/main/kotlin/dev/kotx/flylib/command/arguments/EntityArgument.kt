/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command.arguments

import com.mojang.brigadier.context.CommandContext
import dev.kotx.flylib.command.Argument
import dev.kotx.flylib.command.SuggestionAction
import net.minecraft.server.v1_16_R3.ArgumentEntity
import net.minecraft.server.v1_16_R3.CommandListenerWrapper
import org.bukkit.entity.Entity

/**
 *  An argument that takes a player name or entity selector as input.
 *  If a player name that does not exist or an invalid selector is entered, an error will be displayed on the client side, and even if you try to execute it, it will not be accepted.
 *  Expected input: `PlayerName` `@a`, `@r`, `@e[distance=..5]`
 *
 *  Check the following for the specifications of other arguments.
 *  @see Argument
 */
class EntityArgument(
    override val name: String,
    override val suggestion: SuggestionAction?
) : Argument<List<Entity>> {
    override val type: ArgumentEntity = ArgumentEntity.multipleEntities()

    override fun parse(context: CommandContext<CommandListenerWrapper>, key: String) =
        ArgumentEntity.c(context, key).map { it.bukkitEntity }
}