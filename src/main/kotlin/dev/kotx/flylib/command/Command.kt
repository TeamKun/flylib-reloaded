/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command

abstract class Command(
    internal val name: String
) {
    @JvmField
    internal var description: String? = null

    @JvmField
    internal var permission: Permission? = null

    @JvmField
    internal val aliases: MutableList<String> = mutableListOf()

    @JvmField
    internal val usages: MutableList<Usage> = mutableListOf()

    @JvmField
    internal val examples: MutableList<String> = mutableListOf()

    @JvmField
    internal val children: MutableList<Command> = mutableListOf()

    internal var parent: Command? = null

    open fun CommandContext.execute() {
        //sendHelp
    }

    protected fun description(description: String) {
        this.description = description
    }

    protected fun permission(permission: Permission) {
        this.permission = permission
    }

    protected fun usage(builder: UsageAction) {
        val usage = UsageBuilder().apply { builder.apply { initialize() } }.build()
        usages.add(usage)
    }

    protected fun alias(vararg alias: String) {
        this.aliases.addAll(alias)
    }

    protected fun example(vararg example: String) {
        this.examples.addAll(example)
    }

    protected fun children(vararg children: Command) {
        this.children.addAll(children)
    }
}