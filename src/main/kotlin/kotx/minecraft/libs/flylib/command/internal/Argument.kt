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
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentAnchor.a(), tabComplete)

    class Angle(
        name: String, tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentAngle.a(), tabComplete)

    class Block(
        name: String, tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentBlockPredicate.a(), tabComplete)

    class Chat(
        name: String,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentChat.a(), tabComplete)

    class ChatComponent(
        name: String,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentChatComponent.a(), tabComplete)

    class Entity(
        name: String,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentEntity.multipleEntities(), tabComplete)

    class EntityName(
        name: String,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentEntitySummon.a(), tabComplete)

    class Item(
        name: String,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentItemStack.a(), tabComplete)

    class Math(
        name: String,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentMathOperation.a(), tabComplete)

    class MobEffect(
        name: String,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentMobEffect.a(), tabComplete)

    class Particle(
        name: String,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentParticle.a(), tabComplete)

    class Position(
        name: String,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentPosition.a(), tabComplete)

    class Player(
        name: String,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentProfile.a(), tabComplete)

    class Rotation(
        name: String,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentRotation.a(), tabComplete)

    class Vec2(
        name: String,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentVec2.a(), tabComplete)

    class Vec3(
        name: String,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentVec3.a(), tabComplete)

    class UUID(
        name: String,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentUUID.a(), tabComplete)

    class Dimension(
        name: String,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentDimension.a(), tabComplete)

    class Enchantment(
        name: String,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, ArgumentEnchantment.a(), tabComplete)

    class Text(
        name: String,
        type: StringType = StringType.WORD,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(
        name, when (type) {
            StringType.WORD -> StringArgumentType.word()
            StringType.QUOTE -> StringArgumentType.string()
            StringType.PHRASE -> StringArgumentType.greedyString()
        }, tabComplete
    ) {
        enum class StringType {
            WORD, QUOTE, PHRASE
        }
    }

    class Selection(
        name: String,
        vararg selections: String,
    ) : Argument(name, StringArgumentType.string(), tabComplete = {
        selections.filter { it.startsWith(args.lastOrNull() ?: "", true) }
    })

    class Integer(
        name: String,
        min: Int = Int.MIN_VALUE,
        max: Int = Int.MAX_VALUE,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(
        name, IntegerArgumentType.integer(min, max), tabComplete
    )

    class Float(
        name: String,
        min: kotlin.Float = kotlin.Float.MIN_VALUE,
        max: kotlin.Float = kotlin.Float.MAX_VALUE,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, FloatArgumentType.floatArg(min, max), tabComplete)

    class Double(
        name: String,
        min: kotlin.Double = kotlin.Double.MIN_VALUE,
        max: kotlin.Double = kotlin.Double.MAX_VALUE,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, DoubleArgumentType.doubleArg(min, max), tabComplete)

    class Long(
        name: String,
        min: kotlin.Long = kotlin.Long.MIN_VALUE,
        max: kotlin.Long = kotlin.Long.MAX_VALUE,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, LongArgumentType.longArg(min, max), tabComplete)

    class Boolean(
        name: String,
        tabComplete: (CommandContext.() -> List<String>)? = null,
    ) : Argument(name, BoolArgumentType.bool(), tabComplete)
}