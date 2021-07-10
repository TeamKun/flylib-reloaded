/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command.arguments

import com.mojang.brigadier.context.CommandContext
import dev.kotx.flylib.command.*
import net.minecraft.server.v1_16_R3.*
import org.bukkit.entity.Entity

class EntityArgument(
    override val name: String,
    override val suggestion: SuggestionAction?
) : Argument<List<Entity>> {
    override val type = ArgumentEntity.multipleEntities()

    override fun parse(context: CommandContext<CommandListenerWrapper>, key: String) = ArgumentEntity.c(context, key).map { it.bukkitEntity }
}