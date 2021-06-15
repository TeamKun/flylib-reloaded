/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command.internal

import dev.kotx.flylib.command.*

class Usage @JvmOverloads constructor(
    vararg val args: Argument<*>,
    val description: String = "",
    val permission: Permission? = null,
    val playerOnly: Boolean? = null,
    val options: List<Option> = emptyList(),
    val action: (CommandContext.() -> Unit)? = null
)