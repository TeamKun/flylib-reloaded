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
    val action: (CommandContext.() -> Unit)? = null,
) {
    class Anchor(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentAnchor.a(), permission, playerOnly, tabComplete, action)

    class Angle(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentAngle.a(), permission, playerOnly, tabComplete, action)

    class Block(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentBlockPredicate.a(), permission, playerOnly, tabComplete, action)

    class Chat(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentChat.a(), permission, playerOnly, tabComplete, action)

    class ChatComponent(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentChatComponent.a(), permission, playerOnly, tabComplete, action)

    class Entity(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentEntity.multipleEntities(), permission, playerOnly, tabComplete, action)

    class EntityName(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentEntitySummon.a(), permission, playerOnly, tabComplete, action)

    class Item(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentItemStack.a(), permission, playerOnly, tabComplete, action)

    class Math(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentMathOperation.a(), permission, playerOnly, tabComplete, action)

    class MobEffect(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentMobEffect.a(), permission, playerOnly, tabComplete, action)

    class Particle(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentParticle.a(), permission, playerOnly, tabComplete, action)

    class Position(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentPosition.a(), permission, playerOnly, tabComplete, action)

    class Player(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentProfile.a(), permission, playerOnly, tabComplete, action)

    class Rotation(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentRotation.a(), permission, playerOnly, tabComplete, action)

    class Vec2(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentVec2.a(), permission, playerOnly, tabComplete, action)

    class Vec3(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentVec3.a(), permission, playerOnly, tabComplete, action)

    class UUID(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentUUID.a(), permission, playerOnly, tabComplete, action)

    class Dimension(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentDimension.a(), permission, playerOnly, tabComplete, action)

    class Enchantment(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, ArgumentEnchantment.a(), permission, playerOnly, tabComplete, action)

    class Text(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, StringArgumentType.string(), permission, playerOnly, tabComplete, action)

    class Selection(
        name: String,
        vararg selections: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, StringArgumentType.string(), permission, playerOnly, tabComplete = {
        selections.filter { it.startsWith(args.lastOrNull() ?: "", true) }
    }, action)

    class Integer(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, IntegerArgumentType.integer(), permission, playerOnly, tabComplete, action)

    class Float(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, FloatArgumentType.floatArg(), permission, playerOnly, tabComplete, action)

    class Double(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, DoubleArgumentType.doubleArg(), permission, playerOnly, tabComplete, action)

    class Long(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, LongArgumentType.longArg(), permission, playerOnly, tabComplete, action)

    class Boolean(
        name: String,
        permission: Permission = Permission.EVERYONE,
        playerOnly: kotlin.Boolean = false,
        tabComplete: (CommandContext.() -> List<String>)? = null,
        action: (CommandContext.() -> Unit)? = null,
    ) : Argument(name, BoolArgumentType.bool(), permission, playerOnly, tabComplete, action)
}