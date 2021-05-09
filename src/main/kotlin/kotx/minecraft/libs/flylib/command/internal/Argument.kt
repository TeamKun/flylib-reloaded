/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command.internal

import com.mojang.brigadier.arguments.*
import kotx.minecraft.libs.flylib.command.CommandContext
import kotx.minecraft.libs.flylib.command.internal.arguments.SelectionArgumentType
import net.minecraft.server.v1_16_R3.*

open class Argument(
    val name: String,
    val type: ArgumentType<*>? = null,
    val permission: Permission = Permission.EVERYONE,
    val playerOnly: kotlin.Boolean = false,
    val action: (CommandContext.() -> Unit)? = null,
) {
    class Anchor(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentAnchor.a(), permission, playerOnly, action)

    class Angle(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentAngle.a(), permission, playerOnly, action)

    class Block(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentBlockPredicate.a(), permission, playerOnly, action)

    class Chat(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentChat.a(), permission, playerOnly, action)

    class ChatComponent(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentChatComponent.a(), permission, playerOnly, action)

    class Entity(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentEntity.multipleEntities(), permission, playerOnly, action)

    class EntityName(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentEntitySummon.a(), permission, playerOnly, action)

    class Item(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentItemStack.a(), permission, playerOnly, action)

    class Math(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentMathOperation.a(), permission, playerOnly, action)

    class MobEffect(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentMobEffect.a(), permission, playerOnly, action)

    class Particle(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentParticle.a(), permission, playerOnly, action)

    class Position(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentPosition.a(), permission, playerOnly, action)

    class Player(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentProfile.a(), permission, playerOnly, action)

    class Rotation(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentRotation.a(), permission, playerOnly, action)

    class Vec2(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentVec2.a(), permission, playerOnly, action)

    class Vec3(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentVec3.a(), permission, playerOnly, action)

    class UUID(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentUUID.a(), permission, playerOnly, action)

    class Dimension(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentDimension.a(), permission, playerOnly, action)

    class Enchantment(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentEnchantment.a(), permission, playerOnly, action)

    class Text(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, StringArgumentType.string(), permission, playerOnly, action)

    class Integer(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, IntegerArgumentType.integer(), permission, playerOnly, action)

    class Float(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, FloatArgumentType.floatArg(), permission, playerOnly, action)

    class Double(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, DoubleArgumentType.doubleArg(), permission, playerOnly, action)

    class Long(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, LongArgumentType.longArg(), permission, playerOnly, action)

    class Boolean(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, BoolArgumentType.bool(), permission, playerOnly, action)

    class Selection(
        name: String,
        selections: List<String>,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, SelectionArgumentType.selection(selections), permission, playerOnly, action)
}