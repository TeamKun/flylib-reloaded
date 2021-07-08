/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command

abstract class Command(
    val name: String
) {
    @JvmField
    var description: String? = null

    @JvmField
    var permission: Permission? = null

    @JvmField
    val aliases: MutableList<String> = mutableListOf()

    @JvmField
    val usages: MutableList<Usage> = mutableListOf()

    @JvmField
    val examples: MutableList<String> = mutableListOf()

    @JvmField
    val children: MutableList<Command> = mutableListOf()

    internal var parent: Command? = null

    fun CommandContext.execute() {
        //sendHelp
    }
}