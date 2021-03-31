/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command

/**
 * Permission to use the command. Permission.OP can be used only by OP, Permission.NOT_OP can be used by everyone except OP, and Permission.EVERYONE can be used by everyone.
 */
enum class Permission {
    /**
     * Only OP can be executed.
     */
    OP,

    /**
     * Everyone except OP can do it. (OP cannot be executed.)
     */
    NOT_OP,

    /**
     * Everyone can do it regardless of OP or not OP.
     */
    EVERYONE
}