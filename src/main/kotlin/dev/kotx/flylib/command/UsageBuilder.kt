/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command

class UsageBuilder {
    private val arguments = mutableListOf<Argument<*>>()
    private var description: String? = null
    private var permission: Permission? = null
    private var action: ((CommandContext) -> Unit)? = null

    internal fun build() = Usage(arguments, description, permission, action)
}