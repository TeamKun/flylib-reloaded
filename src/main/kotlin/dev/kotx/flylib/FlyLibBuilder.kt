/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
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
class FlyLibBuilder(
    private val plugin: JavaPlugin
) {
    private val commands = mutableListOf<Command>()
    private val listenerActions = mutableMapOf<HandlerList, Pair<RegisteredListener, Class<*>>>()
    private var defaultPermission = Permission.OP
    private var configObject: Any? = null
    private var configBaseCommandName: String? = null

    /**
     * Add a command.
     */
    fun command(vararg command: Command): FlyLibBuilder {
        command.forEach {
            it.children.setParent(it)
            this.commands.add(it)
        }

        return this
    }

    /**
     * Specify the default config.
     */
    fun config(configObject: Any, baseCommandName: String? = null): FlyLibBuilder {
        this.configObject = configObject
        this.configBaseCommandName = baseCommandName

        return this
    }

    /**
     * Specifies the default permissions that will be assigned if the command permissions are not specified.
     */
    fun defaultPermission(permission: Permission): FlyLibBuilder {
        defaultPermission = permission
        return this
    }

    /**
     * Listens to the specified event with the specified priority. The event Listen is registered when the calling plugin is enabled, not when this method is called, and is unregistered when the calling plugin is disabled.
     */
    @Suppress("UNCHECKED_CAST")
    @JvmOverloads
    fun <T : Event> listen(
        clazz: Class<T>,
        priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = false,
        action: ListenerAction<T>
    ): FlyLibBuilder {
        val handlerList =
            clazz.methods.find { it.name == "getHandlerList" }?.invoke(null) as? HandlerList ?: return this
        val listener = RegisteredListener(
            object : Listener {},
            { _, event -> action.execute(event as T) },
            priority,
            plugin,
            ignoreCancelled
        )

        listenerActions[handlerList] = listener to clazz

        return this
    }

    private fun List<Command>.setParent(parent: Command): Unit = forEach {
        it.parent = parent
        it.children.setParent(it)
    }

    internal fun build(): FlyLib = FlyLibImpl(plugin, commands, defaultPermission, configObject, configBaseCommandName, listenerActions)
}

/**
 * An interface that takes FlyLibBuilder as an argument.
 * In Java, it can be used for SAM conversion.
 */
fun interface FlyLibAction {
    /**
     * An method which replacing kotlin apply block.
     */
    fun FlyLibBuilder.initialize()
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