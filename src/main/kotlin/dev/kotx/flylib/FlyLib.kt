/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib

import dev.kotx.flylib.command.*
import org.bukkit.plugin.java.*

interface FlyLib {
    val plugin: JavaPlugin
    val commandHandler: CommandHandler

    companion object {
        @JvmStatic
        fun create(plugin: JavaPlugin, commands: List<Command>): FlyLib = FlyLibImpl(plugin, commands)
    }
}