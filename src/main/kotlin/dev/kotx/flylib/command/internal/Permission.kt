/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command.internal

import org.bukkit.permissions.*

class Permission(
    val name: String? = null,
    val default: PermissionDefault = PermissionDefault.OP,
) {
    companion object {
        @JvmField
        val OP = Permission(default = PermissionDefault.OP)

        @JvmField
        val EVERYONE = Permission(default = PermissionDefault.TRUE)

        @JvmStatic
        @JvmOverloads
        fun create(id: String? = null, default: PermissionDefault) = Permission(id, default)
    }
}