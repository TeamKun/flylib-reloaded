/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command

import org.bukkit.permissions.PermissionDefault

/**
 * Command permissions.
 */
class Permission(
    /**
     * Custom name for permissions. If nothing is specified (if null), permissions will be automatically added according to the command name or Usage argument name.
     */
    val name: String? = null,
    /**
     * Permission request line. Check the following for details.
     *
     * @see PermissionDefault
     */
    val defaultPermission: PermissionDefault
) {

    companion object {
        /**
         * Permission that only OP can execute.
         */
        @JvmStatic
        val OP = Permission(null, PermissionDefault.OP)

        /**
         * Privileges that everyone can do.
         */
        @JvmStatic
        val EVERYONE = Permission(null, PermissionDefault.TRUE)
    }
}