/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command.internal

import org.bukkit.permissions.*


/**
 * Permission to use the command. Permission.OP can be used only by OP, Permission.NOT_OP can be used by everyone except OP, and Permission.EVERYONE can be used by everyone.
 */
class Permission(
    val id: String,
    val default: PermissionDefault = PermissionDefault.OP,
) {
    companion object {
        @JvmField
        val OP = Permission("op", PermissionDefault.OP)
        @JvmField
        val EVERYONE = Permission("everyone", PermissionDefault.TRUE)
        @JvmStatic
        fun create(id: String) = Permission(id)
    }
}