/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib

import dev.kotx.flylib.command.Command
import dev.kotx.flylib.command.CommandHandlerImpl
import dev.kotx.flylib.command.Permission
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.event.server.PluginEnableEvent
import org.bukkit.plugin.RegisteredListener
import org.bukkit.plugin.java.JavaPlugin
import org.koin.dsl.module

internal class FlyLibImpl(override val plugin: JavaPlugin, commands: List<Command>, defaultPermission: Permission) :
    FlyLib {
    override val commandHandler = CommandHandlerImpl(this, commands, defaultPermission)

    init {
        println("Loading FlyLib...")

        if (FlyLibContext.getOrNull() != null) FlyLibContext.stop()

        FlyLibContext.startKoin {
            modules(module {
                single { plugin }
                single { logger }
                single { commandHandler }
            })
        }

        register<PluginEnableEvent> {
            if (it.plugin == plugin) enable()
        }

        register<PluginDisableEvent> {
            if (it.plugin == plugin) disable()
        }
    }

    private fun enable() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin) { load() }
        commandHandler.enable()
        println()
        println(
            """
             _____ _  __   __
            |  ___| | \ \ / /
            | |_  | |  \ V /   FlyLib Reloaded v0.3.0 by Kotx 
            |  _| | |___| |    inject successfully.
            |_|   |_____|_|  
        """.trimIndent()
        )
        println()
    }

    private fun disable() {
        println("Unloading FlyLib...")
        commandHandler.disable()
        println("FlyLib unloaded successfully.")
    }

    private fun load() {
        commandHandler.load()
    }

    private inline fun <reified T : Event> register(crossinline action: (T) -> Unit) {
        val handlerList = (T::class.java.methods.find { it.name == "getHandlerList" } ?: return).invoke(null) as HandlerList
        val listener = RegisteredListener(
            object : Listener {},
            { _, event -> action(event as T) },
            EventPriority.NORMAL,
            plugin,
            false
        )

        handlerList.register(listener)
    }
}