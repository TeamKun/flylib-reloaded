/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib

import dev.kotx.flylib.command.Command
import dev.kotx.flylib.command.Permission
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.RegisteredListener
import org.bukkit.plugin.java.JavaPlugin

/**
 * A builder that creates Fly Lib.
 */
class FlyLibBuilder<T>(
    private val plugin: JavaPlugin
) {
    private val commands = mutableListOf<Command<T>>()
    private val listenerActions = mutableMapOf<HandlerList, Pair<RegisteredListener, Class<*>>>()
    private var defaultPermission = Permission.OP
    private var config: T? = null
    private var baseCommandName: String? = null

    /**
     * Add a command.
     */
    fun command(vararg command: Command<T>): FlyLibBuilder<T> {
        command.forEach {
            this.commands.add(it)
        }

        return this
    }

    /**
     * Specifies the default permissions that will be assigned if the command permissions are not specified.
     */
    fun defaultPermission(permission: Permission): FlyLibBuilder<T> {
        defaultPermission = permission
        return this
    }

    fun config(baseCommandName: String? = null, config: T): FlyLibBuilder<T> {
        this.baseCommandName = baseCommandName
        this.config = config
        return this
    }

    /**
     * Listens to the specified event with the specified priority. The event Listen is registered when the calling plugin is enabled, not when this method is called, and is unregistered when the calling plugin is disabled.
     */
    @Suppress("UNCHECKED_CAST")
    @JvmOverloads
    fun <E : Event> listen(
        clazz: Class<E>,
        priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = false,
        action: ListenerAction<E>
    ): FlyLibBuilder<T> {
        val handlerList =
            clazz.methods.find { it.name == "getHandlerList" }?.invoke(null) as? HandlerList ?: return this
        val listener = RegisteredListener(
            object : Listener {},
            { _, event -> action.execute(event as E) },
            priority,
            plugin,
            ignoreCancelled
        )

        listenerActions[handlerList] = listener to clazz

        return this
    }

    internal fun build(): FlyLib<T> =
        FlyLibImpl(plugin, commands, defaultPermission, config, baseCommandName, listenerActions)
}

/**
 * An interface that takes FlyLibBuilder as an argument.
 * In Java, it can be used for SAM conversion.
 */
fun interface FlyLibAction<T> {
    /**
     * An method which replacing kotlin apply block.
     */
    fun FlyLibBuilder<T>.initialize()
}

/**
 * A handler that handles events.
 * Java does SAM conversion.
 */
fun interface ListenerAction<T : Event> {
    /**
     * An method which replacing kotlin apply block.
     */
    fun execute(event: T)
}