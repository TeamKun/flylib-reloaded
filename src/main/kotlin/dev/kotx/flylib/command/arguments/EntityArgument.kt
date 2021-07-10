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
 * Argument to select an entity.
 */
class EntityArgument(
        override val name: String,
        override val suggestion: SuggestionAction?
) : Argument<List<Entity>> {
    override val type = ArgumentEntity.multipleEntities()

    override fun parse(context: CommandContext<CommandListenerWrapper>, key: String) = ArgumentEntity.c(context, key).map { it.bukkitEntity }
}