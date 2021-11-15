/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command

import dev.kotx.flylib.command.arguments.BooleanArgument
import dev.kotx.flylib.command.arguments.DoubleArgument
import dev.kotx.flylib.command.arguments.EntityArgument
import dev.kotx.flylib.command.arguments.FloatArgument
import dev.kotx.flylib.command.arguments.IntegerArgument
import dev.kotx.flylib.command.arguments.LiteralArgument
import dev.kotx.flylib.command.arguments.LocationArgument
import dev.kotx.flylib.command.arguments.LongArgument
import dev.kotx.flylib.command.arguments.StringArgument
import dev.kotx.flylib.command.arguments.VectorArgument

/**
 * Builder for creating arguments and definitions.
 */
class UsageBuilder {
    private val arguments = mutableListOf<Argument<*>>()
    private var description: String? = null
    private var permission: Permission? = null
    private var action: ContextAction? = null

    /**
     * Description of arguments and definitions.
     */
    fun description(description: String): UsageBuilder {
        this.description = description
        return this
    }

    /**
     * Permission to execute.
     */
    fun permission(permission: Permission): UsageBuilder {
        this.permission = permission
        return this
    }

    /**
     * What is done.
     */
    fun executes(action: ContextAction): UsageBuilder {
        this.action = action
        return this
    }

    /**
     * Only the text specified in the literal argument literal is allowed, and the user's own input is not accepted.
     */
    fun literalArgument(
        literal: String,
        action: ContextAction? = null
    ): UsageBuilder {
        this.arguments.add(LiteralArgument(literal, action))
        return this
    }

    /**
     * An argument that takes an Int value. The lowest value and the highest value can be specified.
     */
    @JvmOverloads
    fun integerArgument(
        name: String,
        min: Int = Int.MIN_VALUE,
        max: Int = Int.MAX_VALUE,
        suggestion: SuggestionAction? = null,
        action: ContextAction? = null
    ): UsageBuilder {
        this.arguments.add(IntegerArgument(name, min, max, suggestion, action))
        return this
    }

    /**
     * An argument that takes an Int value.
     */
    fun integerArgument(
        name: String, suggestion: SuggestionAction? = null,
        action: ContextAction? = null
    ): UsageBuilder {
        this.arguments.add(IntegerArgument(name, suggestion = suggestion, action = action))
        return this
    }

    /**
     * An argument that takes a Long value. You can specify the lowest and highest values.
     */
    @JvmOverloads
    fun longArgument(
        name: String,
        min: Long = Long.MIN_VALUE,
        max: Long = Long.MAX_VALUE,
        suggestion: SuggestionAction? = null,
        action: ContextAction? = null
    ): UsageBuilder {
        this.arguments.add(LongArgument(name, min, max, suggestion, action))
        return this
    }

    /**
     * An argument that takes a long value.
     */
    fun longArgument(
        name: String, suggestion: SuggestionAction? = null,
        action: ContextAction? = null
    ): UsageBuilder {
        this.arguments.add(LongArgument(name, suggestion = suggestion, action = action))
        return this
    }

    /**
     * An argument that takes a double value. You can specify the lowest and highest values.
     */
    @JvmOverloads
    fun doubleArgument(
        name: String,
        min: Double = Double.MIN_VALUE,
        max: Double = Double.MAX_VALUE,
        suggestion: SuggestionAction? = null,
        action: ContextAction? = null
    ): UsageBuilder {
        this.arguments.add(DoubleArgument(name, min, max, suggestion, action))
        return this
    }

    /**
     * An argument that takes a double value.
     */
    fun doubleArgument(
        name: String, suggestion: SuggestionAction? = null,
        action: ContextAction? = null
    ): UsageBuilder {
        this.arguments.add(DoubleArgument(name, suggestion = suggestion, action = action))
        return this
    }

    /**
     * An argument that takes a float value. You can specify the lowest and highest values.
     */
    @JvmOverloads
    fun floatArgument(
        name: String,
        min: Float = Float.MIN_VALUE,
        max: Float = Float.MAX_VALUE,
        suggestion: SuggestionAction? = null,
        action: ContextAction? = null
    ): UsageBuilder {
        this.arguments.add(FloatArgument(name, min, max, suggestion, action))
        return this
    }

    /**
     * An argument that takes a float value.
     */
    fun floatArgument(
        name: String, suggestion: SuggestionAction? = null,
        action: ContextAction? = null
    ): UsageBuilder {
        this.arguments.add(LongArgument(name, suggestion = suggestion, action = action))
        return this
    }

    /**
     * An argument that takes a String value. Type can be specified.
     */
    @JvmOverloads
    fun stringArgument(
        name: String,
        type: StringArgument.Type = StringArgument.Type.WORD,
        suggestion: SuggestionAction? = null,
        action: ContextAction? = null
    ): UsageBuilder {
        this.arguments.add(StringArgument(name, type, suggestion, action))
        return this
    }

    /**
     * An argument that takes a String value.
     */
    fun stringArgument(
        name: String, suggestion: SuggestionAction? = null,
        action: ContextAction? = null
    ): UsageBuilder {
        this.arguments.add(StringArgument(name, suggestion = suggestion, action = action))
        return this
    }

    /**
     * String argument of the selection formula.
     */
    fun selectionArgument(
        name: String, selections: List<String>,
        action: ContextAction? = null
    ): UsageBuilder {
        this.arguments.add(StringArgument(name, suggestion = { suggestAll(selections) }, action = action))
        return this
    }

    /**
     * String argument of the selection formula.
     */
    fun selectionArgument(
        name: String, vararg selections: String,
        action: ContextAction? = null
    ): UsageBuilder {
        this.arguments.add(StringArgument(name, suggestion = { suggestAll(listOf(*selections)) }, action = action))
        return this
    }

    /**
     * An argument that takes a boolean value.
     */
    fun booleanArgument(
        name: String, suggestion: SuggestionAction? = null,
        action: ContextAction? = null
    ): UsageBuilder {
        this.arguments.add(BooleanArgument(name, suggestion = suggestion, action = action))
        return this
    }

    /**
     * An argument that takes an entity as a value.
     *
     * ## Example
     * `@a[distance=5]` `AnPlayerName` `@r`
     */
    @JvmOverloads
    fun entityArgument(
        name: String,
        enableSelector: Boolean = true,
        enableEntities: Boolean = true,
        suggestion: SuggestionAction? = null,
        action: ContextAction? = null
    ): UsageBuilder {
        this.arguments.add(EntityArgument(name, enableSelector, enableEntities, suggestion, action))
        return this
    }

    /**
     * An argument that takes a coordinate.
     *
     * ## Example
     * `10 235 66` `~ ~10 ~-5` `~ 10 ^200`
     */
    @JvmOverloads
    fun locationArgument(
        name: String, suggestion: SuggestionAction? = null,
        action: ContextAction? = null
    ): UsageBuilder {
        this.arguments.add(LocationArgument(name, suggestion, action))
        return this
    }

    /**
     * Argument that takes a direction.
     *
     * ## Example
     * `5 3 1` `^ ^ ^` `~ ~5 ~`
     */
    @JvmOverloads
    fun vectorArgument(
        name: String, suggestion: SuggestionAction? = null,
        action: ContextAction? = null
    ): UsageBuilder {
        this.arguments.add(VectorArgument(name, suggestion, action))
        return this
    }

    internal fun build() = Usage(arguments, description, permission, action)
}

/**
 * Usage Builder actions
 */
fun interface UsageAction {
    /**
     * An method which replacing kotlin apply block.
     */
    fun UsageBuilder.initialize()
}