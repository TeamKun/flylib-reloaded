/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command

import org.bukkit.permissions.*

abstract class Command(
    val name: String
) {
    @JvmField
    var description: String? = null
    @JvmField
    var permission: Permission? = null
    @JvmField
    val aliases: Array<String> = emptyArray()
    @JvmField
    val usages: MutableList<Usage> = mutableListOf()
}