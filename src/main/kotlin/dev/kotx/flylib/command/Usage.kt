/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command

import org.bukkit.permissions.*

class Usage(
    val arguments: List<Argument<*>>,
    val description: String? = null,
    val permission: Permission? = null,
    val action: (CommandContext.() -> Unit)? = null
)