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

internal class FlyLibImpl(
    override val plugin: JavaPlugin,
    commands: List<Command>,
    defaultPermission: Permission,
    private val listenerActions: MutableMap<HandlerList, RegisteredListener>
) :
    FlyLib {
    override val commandHandler = CommandHandlerImpl(this, commands, defaultPermission)

    init {
        println("Loading FlyLib...")

        listen(PluginEnableEvent::class.java) {
            if (it.plugin == plugin) {
                if (FlyLibContext.getOrNull() != null) FlyLibContext.stopKoin()
                FlyLibContext.startKoin {
                    modules(module {
                        single { plugin }
                        single { logger }
                        single { commandHandler }
                    })
                }

                enable()
            }
        }

        listen(PluginDisableEvent::class.java) {
            if (it.plugin == plugin) {
                if (FlyLibContext.getOrNull() != null) FlyLibContext.stopKoin()

                disable()
            }
        }
    }

    private fun enable() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin) { load() }
        commandHandler.enable()
        listenerActions.forEach {
            it.key.register(it.value)
        }
        println()
        println(
            """
             _____ _  __   __
            |  ___| | \ \ / /
            | |_  | |  \ V /   FlyLib Reloaded v0.3.13 by Kotx 
            |  _| | |___| |    inject successfully.
            |_|   |_____|_|  
        """.trimIndent()
        )
        println()
    }

    private fun disable() {
        println("Unloading FlyLib...")
        commandHandler.disable()
        listenerActions.forEach {
            it.key.unregister(it.value)
        }
        println("FlyLib unloaded successfully.")
    }

    private fun load() {
        commandHandler.load()
    }

    override fun <T : Event> listen(clazz: Class<T>, action: ListenerAction<T>) {
        listen(clazz, EventPriority.NORMAL, action)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Event> listen(clazz: Class<T>, priority: EventPriority, action: ListenerAction<T>) {
        val handlerList =
            (clazz.methods.find { it.name == "getHandlerList" } ?: return).invoke(null) as HandlerList
        val listener = RegisteredListener(
            object : Listener {},
            { _, event -> action.execute(event as T) },
            priority,
            plugin,
            false
        )

        handlerList.register(listener)
    }
}