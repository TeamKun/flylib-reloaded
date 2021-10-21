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
    private val listenerActions: MutableMap<HandlerList, Pair<RegisteredListener, Class<*>>>
) :
    FlyLib {
    override val commandHandler = CommandHandlerImpl(this, commands, defaultPermission)

    init {
        println("\u001B[34m\u001B[1mLoading FlyLib...\u001B[m")
        println()

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
            println("\u001B[32m\u001B[1m[+]\u001B[m\u001B[32m \u001B[1m${it.value.second.simpleName}\u001B[m\u001B[32m was listened with \u001B[1m${it.value.first.priority.name}\u001B[m\u001B[32m priority\u001B[m")
            it.key.register(it.value.first)
        }
        println()
        println(
            """
             [32m_____ _  __   __[m
            [32m|  ___| | \ \ / /[m
            [32m| |_  | |  \ V /   [1mFlyLib Reloaded by Kotx[m
            [32m|  _| | |___| |    [1minject successfully.[m
            [32m|_|   |_____|_|[m  
        """.trimIndent()
        )
        println()
    }

    private fun disable() {
        println("\u001B[34m\u001B[1mUnloading FlyLib...\u001B[m")
        commandHandler.disable()
        listenerActions.forEach {
            it.key.unregister(it.value.first)
            println("\u001B[31m\u001B[1m[-]\u001B[m\u001B[31m A listener \u001B[1m${it.value.second.simpleName}\u001B[m\u001B[31m was unregistered\u001B[m")
        }
        println("\u001B[32m\u001B[1mFlyLib unloaded successfully.\u001B[m")
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