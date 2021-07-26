/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib

import dev.kotx.flylib.command.Command
import dev.kotx.flylib.command.Permission
import org.bukkit.plugin.java.JavaPlugin

/**
 * A builder that creates Fly Lib.
 */
class FlyLibBuilder(
    private val plugin: JavaPlugin
) {
    private val commands = mutableListOf<Command>()
    private var defaultPermission = Permission.OP

    /**
     * Add a command.
     */
    fun command(vararg command: Command): FlyLibBuilder {
        command.forEach {
            it.children.setParent(it)
            this.commands.add(it)
        }

        return this
    }

    /**
     * Specifies the default permissions that will be assigned if the command permissions are not specified.
     */
    fun defaultPermission(permission: Permission): FlyLibBuilder {
        defaultPermission = permission
        return this
    }

    private fun List<Command>.setParent(parent: Command): Unit = forEach {
        it.parent = parent
        it.children.setParent(it)
    }

    internal fun build(): FlyLib = FlyLibImpl(plugin, commands, defaultPermission)
}

/**
 * An interface that takes FlyLibBuilder as an argument.
 * In Java, it can be used for SAM conversion.
 */
fun interface FlyLibAction {
    /**
     * An method which replacing kotlin apply block.
     */
    fun FlyLibBuilder.initialize()
}