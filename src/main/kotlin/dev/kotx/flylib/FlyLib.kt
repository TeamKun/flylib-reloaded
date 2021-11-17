/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib

import dev.kotx.flylib.command.CommandHandler
import dev.kotx.flylib.command.Config
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.plugin.java.JavaPlugin

/**
 * FlyLib interface.
 * Use FlyLibBuilder.
 */
interface FlyLib {
    /**
     * The original plugin that called FlyLib
     */
    val plugin: JavaPlugin

    /**
     * Class that manages commands
     */
    val commandHandler: CommandHandler

    val config: Config?

    /**
     * Listens to the specified event with Event Priority.NORMAL
     */
    fun <T : Event> listen(clazz: Class<T>, action: ListenerAction<T>)

    /**
     * Listens to the specified event with the specified priority.
     */
    fun <T : Event> listen(clazz: Class<T>, priority: EventPriority, action: ListenerAction<T>)

    fun loadConfig()
    fun saveConfig()

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