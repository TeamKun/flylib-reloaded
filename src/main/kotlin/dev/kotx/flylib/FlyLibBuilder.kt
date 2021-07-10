/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib

import dev.kotx.flylib.command.Command
import dev.kotx.flylib.command.Permission
import org.bukkit.plugin.java.JavaPlugin

class FlyLibBuilder(
    val plugin: JavaPlugin
) {
    private val commands = mutableListOf<Command>()
    private var defaultPermission = Permission.OP

    fun command(vararg command: Command): FlyLibBuilder {
        command.forEach {
            it.children.setParent(it)
            this.commands.add(it)
        }

        return this
    }

    fun defaultPermission(permission: Permission): FlyLibBuilder {
        defaultPermission = permission
        return this
    }

    fun List<Command>.setParent(parent: Command): Unit = forEach {
        it.parent = parent
        it.children.setParent(it)
    }

    internal fun build(): FlyLib = FlyLibImpl(plugin, commands, defaultPermission)
}

fun interface FlyLibAction {
    fun FlyLibBuilder.initialize()
}