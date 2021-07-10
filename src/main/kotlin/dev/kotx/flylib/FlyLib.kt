/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib

import dev.kotx.flylib.command.CommandHandler
import org.bukkit.plugin.java.JavaPlugin

interface FlyLib {
    val plugin: JavaPlugin
    val commandHandler: CommandHandler

    companion object {
        @JvmStatic
        fun create(plugin: JavaPlugin, builder: FlyLibAction): FlyLib = FlyLibBuilder(plugin).apply { builder.apply { initialize() } }.build()
    }
}

fun JavaPlugin.flyLib(builder: FlyLibAction) = FlyLib.create(this, builder)