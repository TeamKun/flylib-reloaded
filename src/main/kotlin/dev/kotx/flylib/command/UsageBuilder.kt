/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command

import dev.kotx.flylib.command.arguments.*

class UsageBuilder {
    private val arguments = mutableListOf<Argument<*>>()
    private var description: String? = null
    private var permission: Permission? = null
    private var action: ((CommandContext) -> Unit)? = null

    fun description(description: String): UsageBuilder {
        this.description = description
        return this
    }

    fun permission(permission: Permission): UsageBuilder {
        this.permission = permission
        return this
    }

    fun executes(action: (CommandContext) -> Unit = {}): UsageBuilder {
        this.action = action
        return this
    }

    fun literalArgument(literal: String): UsageBuilder {
        this.arguments.add(LiteralArgument(literal))
        return this
    }

    fun integerArgument(name: String, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE, suggestion: SuggestionAction? = null): UsageBuilder {
        this.arguments.add(IntegerArgument(name, min, max, suggestion))
        return this
    }

    fun longArgument(name: String, min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE, suggestion: SuggestionAction? = null): UsageBuilder {
        this.arguments.add(LongArgument(name, min, max, suggestion))
        return this
    }

    fun textArgument(name: String, type: TextArgument.Type = TextArgument.Type.WORD, suggestion: SuggestionAction? = null): UsageBuilder {
        this.arguments.add(TextArgument(name, type, suggestion))
        return this
    }

    fun entityArgument(name: String, suggestion: SuggestionAction? = null): UsageBuilder {
        this.arguments.add(EntityArgument(name, suggestion))
        return this
    }

    fun locationArgument(name: String, suggestion: SuggestionAction? = null): UsageBuilder {
        this.arguments.add(LocationArgument(name, suggestion))
        return this
    }

    fun vectorArgument(name: String, suggestion: SuggestionAction? = null): UsageBuilder {
        this.arguments.add(VectorArgument(name, suggestion))
        return this
    }

    internal fun build() = Usage(arguments, description, permission, action)
}

fun interface UsageAction {
    fun UsageBuilder.initialize()
}