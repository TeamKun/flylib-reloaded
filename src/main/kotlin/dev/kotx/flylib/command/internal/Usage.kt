/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command.internal

import dev.kotx.flylib.command.*

class Usage @JvmOverloads constructor(
    val args: Array<Argument<*>>,
    val description: String = "",
    val permission: Permission? = null,
    val playerOnly: Boolean? = null,
    val options: List<Option> = emptyList(),
    val action: CommandAction? = null
) {
    @JvmOverloads
    constructor(
        args: Array<Argument<*>>,
        description: String = "",
        permission: Permission? = null,
        playerOnly: Boolean? = null,
        action: CommandAction?
    ) : this(
        args, description, permission, playerOnly, emptyList(), action
    )
}