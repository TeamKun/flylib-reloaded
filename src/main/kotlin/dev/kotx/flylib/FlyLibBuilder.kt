/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib

import dev.kotx.flylib.command.*
import org.bukkit.plugin.java.*

class FlyLibBuilder(
    val plugin: JavaPlugin
) {
    private val commands = mutableListOf<Command>()
    private var defaultPermission = Permission.OP

    fun command(vararg command: Command): FlyLibBuilder {
        commands.addAll(command)
        return this
    }

    fun defaultPermission(permission: Permission): FlyLibBuilder {
        defaultPermission = permission
        return this
    }

    internal fun build(): FlyLib = FlyLibImpl(plugin, commands, defaultPermission)
}

fun interface FlyLibAction {
    fun FlyLibBuilder.initialize()
}