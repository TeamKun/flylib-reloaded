/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib

import dev.kotx.flylib.command.Command
import dev.kotx.flylib.command.CommandHandlerImpl
import dev.kotx.flylib.command.Permission
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.event.server.PluginEnableEvent
import org.bukkit.event.server.ServerLoadEvent
import org.bukkit.plugin.RegisteredListener
import org.bukkit.plugin.java.JavaPlugin
import org.koin.dsl.module
import org.slf4j.LoggerFactory

internal class FlyLibImpl(override val plugin: JavaPlugin, commands: List<Command>, defaultPermission: Permission) : FlyLib {
    private val logger = LoggerFactory.getLogger("FlyLib Reloaded")
    override val commandHandler = CommandHandlerImpl(this, commands, defaultPermission)

    init {
        logger.info("Loading FlyLib...")

        if (FlyLibContext.getOrNull() != null)
            FlyLibContext.stop()

        FlyLibContext.startKoin {
            modules(module {
                single { plugin }
                single { logger }
                single { commandHandler }
            })
        }

        register<PluginEnableEvent> {
            if (it.plugin == plugin)
                enable()
        }

        register<PluginDisableEvent> {
            if (it.plugin == plugin)
                disable()
        }

        register<ServerLoadEvent> {
            load()
        }
    }

    private fun enable() {
        commandHandler.enable()
        println()
        println(
                """
              ______ _
             |  ____| |
             | |__  | |      FlyLib Reloaded v0.3.0
             |  __| | |      by Kotx
             | |    | |____
             |_|    |______|
        """.trimIndent()
        )
        println()
    }

    private fun disable() {
        logger.info("Unloading FlyLib...")
        commandHandler.disable()
        logger.info("FlyLib unloaded successfully.")
    }

    private fun load() {
        commandHandler.load()
    }

    private inline fun <reified T : Event> register(crossinline action: (T) -> Unit) {
        val handlerList = T::class.java.methods.find { it.name == "getHandlerList" }!!.invoke(null) as HandlerList
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