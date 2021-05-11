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
    val tabComplete: (CommandContext.() -> List<String>)? = null,
) {
    class Anchor(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentAnchor.a(), tabComplete)

    class Angle(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentAngle.a(), tabComplete)

    class Block(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentBlockPredicate.a(), tabComplete)

    class Chat(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentChat.a(), tabComplete)

    class ChatComponent(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentChatComponent.a(), tabComplete)

    class Entity(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentEntity.multipleEntities(), tabComplete)

    class EntityName(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentEntitySummon.a(), tabComplete)

    class Item(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentItemStack.a(), tabComplete)

    class Math(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentMathOperation.a(), tabComplete)

    class MobEffect(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentMobEffect.a(), tabComplete)

    class Particle(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentParticle.a(), tabComplete)

    class Position(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentPosition.a(), tabComplete)

    class Player(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentProfile.a(), tabComplete)

    class Rotation(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentRotation.a(), tabComplete)

    class Vec2(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentVec2.a(), tabComplete)

    class Vec3(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentVec3.a(), tabComplete)

    class UUID(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentUUID.a(), tabComplete)

    class Dimension(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentDimension.a(), tabComplete)

    class Enchantment(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentEnchantment.a(), tabComplete)

    class Text(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, StringArgumentType.string(), tabComplete)

    class Selection(
        name: String,
        vararg selections: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
    ) : Argument(name, StringArgumentType.string(), tabComplete = {
        selections.filter { it.startsWith(args.lastOrNull() ?: "", true) }
    })

    class Integer(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, IntegerArgumentType.integer(), tabComplete)

    class Float(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, FloatArgumentType.floatArg(), tabComplete)

    class Double(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, DoubleArgumentType.doubleArg(), tabComplete)

    class Long(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, LongArgumentType.longArg(), tabComplete)

    class Boolean(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, BoolArgumentType.bool(), tabComplete)
}