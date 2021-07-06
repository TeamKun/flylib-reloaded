/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command.internal

import org.bukkit.permissions.*

class Permission(
    val default: PermissionDefault = PermissionDefault.OP,
) {
    companion object {
        @JvmField
        val OP = Permission(PermissionDefault.OP)
        @JvmField
        val EVERYONE = Permission(PermissionDefault.TRUE)
        @JvmStatic
        fun create(id: String) = Permission(id)
    }
}