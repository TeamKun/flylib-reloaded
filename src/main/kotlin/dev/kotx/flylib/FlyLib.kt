/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib

import dev.kotx.flylib.command.CommandHandler
import org.bukkit.plugin.java.JavaPlugin

/**
 * FlyLib interface.
 * Use FlyLibBuilder.
 */
interface FlyLib {
    val plugin: JavaPlugin
    val commandHandler: CommandHandler

    companion object {
        /**
         * Start FlyLib with the specified plugin. It also supports disabling and enabling plug-ins by Plug Man, etc.
         */
        @JvmStatic
        fun create(plugin: JavaPlugin, builder: FlyLibAction): FlyLib =
                FlyLibBuilder(plugin).apply { builder.apply { initialize() } }.build()
    }
}

/**
 * Start FlyLib.
 * This is a function for Kotlin.
 */
fun JavaPlugin.flyLib(builder: FlyLibAction) = FlyLib.create(this, builder)