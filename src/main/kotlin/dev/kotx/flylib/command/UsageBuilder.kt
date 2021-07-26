/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command

import dev.kotx.flylib.command.arguments.EntityArgument
import dev.kotx.flylib.command.arguments.IntegerArgument
import dev.kotx.flylib.command.arguments.LiteralArgument
import dev.kotx.flylib.command.arguments.LocationArgument
import dev.kotx.flylib.command.arguments.LongArgument
import dev.kotx.flylib.command.arguments.TextArgument
import dev.kotx.flylib.command.arguments.VectorArgument

/**
 * Builder for creating arguments and definitions.
 */
class UsageBuilder {
    private val arguments = mutableListOf<Argument<*>>()
    private var description: String? = null
    private var permission: Permission? = null
    private var action: ((CommandContext) -> Unit)? = null

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
    fun executes(action: (CommandContext) -> Unit = {}): UsageBuilder {
        this.action = action
        return this
    }

    /**
     * Only the text specified in the literal argument literal is allowed, and the user's own input is not accepted.
     */
    fun literalArgument(literal: String): UsageBuilder {
        this.arguments.add(LiteralArgument(literal))
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
        suggestion: SuggestionAction? = null
    ): UsageBuilder {
        this.arguments.add(IntegerArgument(name, min, max, suggestion))
        return this
    }

    /**
     * An argument that takes an Int value.
     */
    fun integerArgument(name: String, suggestion: SuggestionAction? = null): UsageBuilder {
        this.arguments.add(IntegerArgument(name, suggestion = suggestion))
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
        suggestion: SuggestionAction? = null
    ): UsageBuilder {
        this.arguments.add(LongArgument(name, min, max, suggestion))
        return this
    }

    /**
     * An argument that takes a Long value.
     */
    fun longArgument(name: String, suggestion: SuggestionAction? = null): UsageBuilder {
        this.arguments.add(LongArgument(name, suggestion = suggestion))
        return this
    }

    /**
     * An argument that takes a String value. Type can be specified.
     */
    @JvmOverloads
    fun textArgument(
        name: String,
        type: TextArgument.Type = TextArgument.Type.WORD,
        suggestion: SuggestionAction? = null
    ): UsageBuilder {
        this.arguments.add(TextArgument(name, type, suggestion))
        return this
    }

    /**
     * An argument that takes a String value.
     */
    fun textArgument(name: String, suggestion: SuggestionAction? = null): UsageBuilder {
        this.arguments.add(TextArgument(name, suggestion = suggestion))
        return this
    }

    /**
     * An argument that takes an entity as a value.
     *
     * ## Example
     * `@a[distance=5]` `AnPlayerName` `@r`
     */
    @JvmOverloads
    fun entityArgument(name: String, suggestion: SuggestionAction? = null): UsageBuilder {
        this.arguments.add(EntityArgument(name, suggestion))
        return this
    }

    /**
     * An argument that takes a coordinate.
     *
     * ## Example
     * `10 235 66` `~ ~10 ~-5` `~ 10 ^200`
     */
    @JvmOverloads
    fun locationArgument(name: String, suggestion: SuggestionAction? = null): UsageBuilder {
        this.arguments.add(LocationArgument(name, suggestion))
        return this
    }

    /**
     * Argument that takes a direction.
     *
     * ## Example
     * `5 3 1` `^ ^ ^` `~ ~5 ~`
     */
    @JvmOverloads
    fun vectorArgument(name: String, suggestion: SuggestionAction? = null): UsageBuilder {
        this.arguments.add(VectorArgument(name, suggestion))
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