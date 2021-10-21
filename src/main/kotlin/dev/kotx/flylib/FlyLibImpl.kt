/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib

import dev.kotx.flylib.command.Command
import dev.kotx.flylib.command.CommandHandlerImpl
import dev.kotx.flylib.command.Permission
import dev.kotx.flylib.util.BOLD
import dev.kotx.flylib.util.CYAN
import dev.kotx.flylib.util.GREEN
import dev.kotx.flylib.util.RED
import dev.kotx.flylib.util.RESET
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
        println("$CYAN${BOLD}Loading FlyLib...$RESET")

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
        println("  $CYAN${BOLD}Listeners registered:$RESET")
        listenerActions.forEach {
            it.key.register(it.value.first)
            println("    $GREEN${BOLD}[+]$RESET$GREEN ${it.value.second.simpleName} (${it.value.first.priority.name})$RESET")
        }
        println()
        println(
            """
             ${GREEN}_____ _  __   __$RESET
            ${GREEN}|  ___| | \ \ / /$RESET
            ${GREEN}| |_  | |  \ V /   ${BOLD}FlyLib Reloaded by Kotx$RESET
            ${GREEN}|  _| | |___| |    ${BOLD}inject successfully.$RESET
            ${GREEN}|_|   |_____|_|$RESET  
        """.trimIndent()
        )
    }

    private fun disable() {
        println("$CYAN${BOLD}Unloading FlyLib...$RESET")
        commandHandler.disable()
        println("  $CYAN${BOLD}Listeners unregistered:$RESET")
        listenerActions.forEach {
            it.key.unregister(it.value.first)
            println("    $RED${BOLD}[+]$RESET$RED ${it.value.second.simpleName} (${it.value.first.priority.name})$RESET")
        }
        println()
        println("$GREEN${BOLD}FlyLib unloaded successfully.$RESET")
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