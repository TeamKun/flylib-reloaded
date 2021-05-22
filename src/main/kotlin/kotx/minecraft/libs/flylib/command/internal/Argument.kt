/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command.internal

import com.mojang.authlib.GameProfile
import com.mojang.brigadier.arguments.*
import kotx.minecraft.libs.flylib.command.CommandContext
import net.minecraft.server.v1_16_R3.*
import java.util.function.Predicate

typealias Context = com.mojang.brigadier.context.CommandContext<CommandListenerWrapper>

open class Argument<T>(
    val name: String,
    val type: ArgumentType<*>? = null,
    val parser: (Context, String) -> T,
    val tabComplete: (CommandContext.() -> List<Suggestion>)? = null,
) {
    class Anchor(
        name: String,
        tabComplete: (CommandContext.() -> List<Suggestion>)? = null,
    ) : Argument<ArgumentAnchor.Anchor>(
        name,
        ArgumentAnchor.a(),
        { ctx: Context, key: String -> ArgumentAnchor.a(ctx, key) },
        tabComplete
    )

    class Angle(
        name: String, tabComplete: (CommandContext.() -> List<Suggestion>)? = null,
    ) : Argument<kotlin.Float>(
        name,
        ArgumentAngle.a(), { ctx: Context, key: String -> ArgumentAngle.a(ctx, key) },
        tabComplete
    )

    class Block(
        name: String, tabComplete: (CommandContext.() -> List<Suggestion>)? = null,
    ) : Argument<Predicate<ShapeDetectorBlock>>(
        name,
        ArgumentBlockPredicate.a(), { ctx: Context, key: String -> ArgumentBlockPredicate.a(ctx, key) },
        tabComplete
    )

    class Chat(
        name: String,
        tabComplete: (CommandContext.() -> List<Suggestion>)? = null,
    ) : Argument<IChatBaseComponent>(
        name,
        ArgumentChat.a(),
        { ctx: Context, key: String -> ArgumentChat.a(ctx, key) },
        tabComplete
    )

    class ChatComponent(
        name: String,
        tabComplete: (CommandContext.() -> List<Suggestion>)? = null,
    ) : Argument<IChatBaseComponent>(
        name,
        ArgumentChatComponent.a(),
        { ctx: Context, key: String -> ArgumentChatComponent.a(ctx, key) },
        tabComplete
    )

    class Entity(
        name: String,
        tabComplete: (CommandContext.() -> List<Suggestion>)? = null,
    ) : Argument<net.minecraft.server.v1_16_R3.Entity>(
        name,
        ArgumentEntity.multipleEntities(),
        { ctx: Context, key: String -> ArgumentEntity.a(ctx, key) },
        tabComplete
    )

    class EntityName(
        name: String,
        tabComplete: (CommandContext.() -> List<Suggestion>)? = null,
    ) : Argument<MinecraftKey>(name, ArgumentEntitySummon.a(), { ctx: Context, key: String ->
        ArgumentEntitySummon.a(ctx, key)
    }, tabComplete)

    class Item(
        name: String,
        tabComplete: (CommandContext.() -> List<Suggestion>)? = null,
    ) : Argument<ArgumentPredicateItemStack>(name, ArgumentItemStack.a(), { ctx: Context, key: String ->
        ArgumentItemStack.a(ctx, key)
    }, tabComplete)

    class Math(
        name: String,
        tabComplete: (CommandContext.() -> List<Suggestion>)? = null,
    ) : Argument<ArgumentMathOperation.a>(name, ArgumentMathOperation.a(), { ctx: Context, key: String ->
        ArgumentMathOperation.a(ctx, key)
    }, tabComplete)

    class MobEffect(
        name: String,
        tabComplete: (CommandContext.() -> List<Suggestion>)? = null,
    ) : Argument<MobEffectList>(name, ArgumentMobEffect.a(), { ctx: Context, key: String ->
        ArgumentMobEffect.a(ctx, key)
    }, tabComplete)

    class Particle(
        name: String,
        tabComplete: (CommandContext.() -> List<Suggestion>)? = null,
    ) : Argument<ParticleParam>(name, ArgumentParticle.a(), { ctx: Context, key: String ->
        ArgumentParticle.a(ctx, key)
    }, tabComplete)

    class Position(
        name: String,
        tabComplete: (CommandContext.() -> List<Suggestion>)? = null,
    ) : Argument<BlockPosition>(name, ArgumentPosition.a(), { ctx: Context, key: String ->
        ArgumentPosition.a(ctx, key)
    }, tabComplete)

    class Player(
        name: String,
        tabComplete: (CommandContext.() -> List<Suggestion>)? = null,
    ) : Argument<Collection<GameProfile>>(name, ArgumentProfile.a(), { ctx: Context, key: String ->
        ArgumentProfile.a(ctx, key)
    }, tabComplete)

    class Rotation(
        name: String,
        tabComplete: (CommandContext.() -> List<Suggestion>)? = null,
    ) : Argument<IVectorPosition>(name, ArgumentRotation.a(), { ctx: Context, key: String ->
        ArgumentRotation.a(ctx, key)
    }, tabComplete)

    class Vec2(
        name: String,
        tabComplete: (CommandContext.() -> List<Suggestion>)? = null,
    ) : Argument<Vec2F>(name, ArgumentVec2.a(), { ctx: Context, key: String ->
        ArgumentVec2.a(ctx, key)
    }, tabComplete)

    class Vec3(
        name: String,
        tabComplete: (CommandContext.() -> List<Suggestion>)? = null,
    ) : Argument<Vec3D>(name, ArgumentVec3.a(), { ctx: Context, key: String ->
        ArgumentVec3.a(ctx, key)
    }, tabComplete)

    class UUID(
        name: String,
        tabComplete: (CommandContext.() -> List<Suggestion>)? = null,
    ) : Argument<java.util.UUID>(name, ArgumentUUID.a(), { ctx: Context, key: String ->
        ArgumentUUID.a(ctx, key)
    }, tabComplete)

    class Dimension(
        name: String,
        tabComplete: (CommandContext.() -> List<Suggestion>)? = null,
    ) : Argument<WorldServer>(name, ArgumentDimension.a(), { ctx: Context, key: String ->
        ArgumentDimension.a(ctx, key)
    }, tabComplete)

    class Enchantment(
        name: String,
        tabComplete: (CommandContext.() -> List<Suggestion>)? = null,
    ) : Argument<net.minecraft.server.v1_16_R3.Enchantment>(
        name,
        ArgumentEnchantment.a(),
        { ctx: Context, key: String ->
            ArgumentEnchantment.a(ctx, key)
        },
        tabComplete
    )

    class Text(
        name: String,
        type: StringType = StringType.WORD,
        tabComplete: (CommandContext.() -> List<Suggestion>)? = null,
    ) : Argument<String>(
        name, when (type) {
            StringType.WORD -> StringArgumentType.word()
            StringType.QUOTE -> StringArgumentType.string()
            StringType.PHRASE -> StringArgumentType.greedyString()
        }, { ctx: Context, key: String ->
            StringArgumentType.getString(ctx, key)
        }, tabComplete
    ) {
        enum class StringType {
            WORD, QUOTE, PHRASE
        }
    }

    class Selection(
        name: String,
        vararg selections: String,
    ) : Argument<String>(name, StringArgumentType.string(), { ctx: Context, key: String ->
        StringArgumentType.getString(ctx, key)
    }, tabComplete = {
        selections.map { Suggestion(it) }
    })

    class Integer(
        name: String,
        min: Int = Int.MIN_VALUE,
        max: Int = Int.MAX_VALUE,
        tabComplete: (CommandContext.() -> List<Suggestion>)? = null,
    ) : Argument<Int>(
        name, IntegerArgumentType.integer(min, max), { ctx: Context, key: String ->
            IntegerArgumentType.getInteger(ctx, key)
        }, tabComplete
    )

    class Float(
        name: String,
        min: kotlin.Float = kotlin.Float.MIN_VALUE,
        max: kotlin.Float = kotlin.Float.MAX_VALUE,
        tabComplete: (CommandContext.() -> List<Suggestion>)? = null,
    ) : Argument<kotlin.Float>(name, FloatArgumentType.floatArg(min, max), { ctx: Context, key: String ->
        FloatArgumentType.getFloat(ctx, key)
    }, tabComplete)

    class Double(
        name: String,
        min: kotlin.Double = kotlin.Double.MIN_VALUE,
        max: kotlin.Double = kotlin.Double.MAX_VALUE,
        tabComplete: (CommandContext.() -> List<Suggestion>)? = null,
    ) : Argument<kotlin.Double>(name, DoubleArgumentType.doubleArg(min, max), { ctx: Context, key: String ->
        DoubleArgumentType.getDouble(ctx, key)
    }, tabComplete)

    class Long(
        name: String,
        min: kotlin.Long = kotlin.Long.MIN_VALUE,
        max: kotlin.Long = kotlin.Long.MAX_VALUE,
        tabComplete: (CommandContext.() -> List<Suggestion>)? = null,
    ) : Argument<kotlin.Long>(name, LongArgumentType.longArg(min, max), { ctx: Context, key: String ->
        LongArgumentType.getLong(ctx, key)
    }, tabComplete)

    class Boolean(
        name: String,
        tabComplete: (CommandContext.() -> List<Suggestion>)? = null,
    ) : Argument<kotlin.Boolean>(name, BoolArgumentType.bool(), { ctx: Context, key: String ->
        BoolArgumentType.getBool(ctx, key)
    }, tabComplete)
}