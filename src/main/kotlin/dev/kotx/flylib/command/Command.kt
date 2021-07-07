/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command

import org.bukkit.permissions.*

abstract class Command(
    val name: String
) {
    internal var description: String? = null
    internal var aliases: List<String> = emptyList()
    internal var permission: Permission? = null
}