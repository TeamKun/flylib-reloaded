/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command.internal

import com.mojang.brigadier.arguments.*
import kotx.minecraft.libs.flylib.command.CommandContext
import net.minecraft.server.v1_16_R3.*

open class Argument(
    val name: String,
    val type: ArgumentType<*>? = null,
    val permission: Permission = Permission.EVERYONE,
    val playerOnly: kotlin.Boolean = false,
    val tabComplete: (CommandContext.() -> List<String>)? = null,
) {
    class Anchor(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentAnchor.a(), permission, playerOnly, tabComplete)

    class Angle(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentAngle.a(), permission, playerOnly, tabComplete)

    class Block(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentBlockPredicate.a(), permission, playerOnly, tabComplete)

    class Chat(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentChat.a(), permission, playerOnly, tabComplete)

    class ChatComponent(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentChatComponent.a(), permission, playerOnly, tabComplete)

    class Entity(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentEntity.multipleEntities(), permission, playerOnly, tabComplete)

    class EntityName(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentEntitySummon.a(), permission, playerOnly, tabComplete)

    class Item(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentItemStack.a(), permission, playerOnly, tabComplete)

    class Math(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentMathOperation.a(), permission, playerOnly, tabComplete)

    class MobEffect(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentMobEffect.a(), permission, playerOnly, tabComplete)

    class Particle(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentParticle.a(), permission, playerOnly, tabComplete)

    class Position(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentPosition.a(), permission, playerOnly, tabComplete)

    class Player(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentProfile.a(), permission, playerOnly, tabComplete)

    class Rotation(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentRotation.a(), permission, playerOnly, tabComplete)

    class Vec2(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentVec2.a(), permission, playerOnly, tabComplete)

    class Vec3(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentVec3.a(), permission, playerOnly, tabComplete)

    class UUID(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentUUID.a(), permission, playerOnly, tabComplete)

    class Dimension(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentDimension.a(), permission, playerOnly, tabComplete)

    class Enchantment(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentEnchantment.a(), permission, playerOnly, tabComplete)

    class Text(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, StringArgumentType.string(), permission, playerOnly, tabComplete)

    class Selection(
        name: String,
        vararg selections: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
    ) : Argument(name, StringArgumentType.string(), permission, playerOnly, tabComplete = {
        selections.filter { it.startsWith(args.lastOrNull() ?: "", true) }
    })

    class Integer(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, IntegerArgumentType.integer(), permission, playerOnly, tabComplete)

    class Float(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, FloatArgumentType.floatArg(), permission, playerOnly, tabComplete)

    class Double(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, DoubleArgumentType.doubleArg(), permission, playerOnly, tabComplete)

    class Long(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, LongArgumentType.longArg(), permission, playerOnly, tabComplete)

    class Boolean(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, BoolArgumentType.bool(), permission, playerOnly, tabComplete)
}