/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib

import dev.kotx.flylib.command.CommandHandler
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.plugin.java.JavaPlugin

/**
 * FlyLib interface.
 * Use FlyLibBuilder.
 */
interface FlyLib<T> {
    /**
     * The original plugin that called FlyLib
     */
    val plugin: JavaPlugin

    /**
     * Class that manages commands
     */
    val commandHandler: CommandHandler<T>

    val config: T?

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
    fun updateConfig(action: T.() -> Unit)

    companion object {
        /**
         * Start FlyLib with the specified plugin. It also supports disabling and enabling plug-ins by Plug Man, etc.
         */
        @JvmStatic
        fun <T> create(plugin: JavaPlugin, builder: FlyLibAction<T>): FlyLib<T> =
            FlyLibBuilder<T>(plugin).apply { builder.apply { initialize() } }.build()
    }
}

/**
 * Start FlyLib.
 * This is a function for Kotlin.
 */
fun <T> JavaPlugin.flyLib(builder: FlyLibAction<T>) = FlyLib.create(this, builder)