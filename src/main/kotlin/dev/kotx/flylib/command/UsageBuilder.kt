/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command

import org.bukkit.permissions.*

class UsageBuilder {
    private val arguments = mutableListOf<Argument<*>>()
    private var description: String? = null
    private var permission: Permission? = null
    private var action: (() -> Unit)? = null

    internal fun build() = Usage(arguments, description, permission, action)
}