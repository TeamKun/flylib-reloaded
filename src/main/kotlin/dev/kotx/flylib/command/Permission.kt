/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command

import org.bukkit.permissions.PermissionDefault

class Permission(
    val name: String? = null,
    val defaultPermission: PermissionDefault
) {

    companion object {
        @JvmStatic
        val OP = Permission(null, PermissionDefault.OP)

        @JvmStatic
        val EVERYONE = Permission(null, PermissionDefault.TRUE)
    }
}