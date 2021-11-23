/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command

/**
 * Command usage and definition
 */
class Usage<T>(
    /**
     * A list of arguments. This is also used for the definition.
     */
    val arguments: List<Argument<*, T>>,
    /**
     * Explanation of usage and definition. Used in the default help message.
     */
    val description: String? = null,
    /**
     * The privileges required to execute this definition. The default is specified by the FlyLibBuilder defaultPermission.
     */
    val permission: Permission? = null,
    /**
     * What will be executed if you enter this definition.
     */
    val action: ContextAction<T>? = null
)