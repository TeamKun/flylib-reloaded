/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib

import dev.kotx.flylib.command.*
import org.bukkit.*
import org.bukkit.event.*
import org.bukkit.event.server.*
import org.bukkit.plugin.*
import org.bukkit.plugin.java.*
import org.koin.dsl.*
import org.slf4j.*
import kotlin.reflect.*
import kotlin.reflect.full.*

class FlyLib(
    private val plugin: JavaPlugin,
    private val commandHandler: CommandHandler,
    private val handlers: Map<KClass<out Event>, List<Pair<Builder.ListenerAction<in Event>, EventPriority>>>,
) {
    private val logger = LoggerFactory.getLogger("::FlyLib Reloaded::")!!

    private val registeredListeners = mutableMapOf<KClass<out Event>, MutableList<RegisteredListener>>()

    init {
        koinApplication {
            modules(module {
                single { this@FlyLib }
                single { this@FlyLib.plugin }
                single { this@FlyLib.commandHandler }
                single { this@FlyLib.logger }
            })
        }.also {
            if (FlyLibContext.getOrNull() != null)
                FlyLibContext.stop()

            FlyLibContext.startKoin(FlyLibContext, it)
        }

        registerListener<PluginEnableEvent> {
            if (it.plugin.name == this.plugin.name)
                onEnable()
        }

        registerListener<PluginDisableEvent> {
            if (it.plugin.name == this.plugin.name)
                onDisable()
        }
    }

    private fun onEnable() {
        try {
            logger.info("Injecting...")

            handlers.map { (event, actions) ->
                registeredListeners[event] to actions.map {
                    registerListener(it.second, it.first)
                }
            }

            commandHandler.onEnable()

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin) {
                onLoad()
            }

            println(
                """
                    
                ______ _       _     _ _      ______     _                 _          _ 
                |  ___| |     | |   (_) |     | ___ \   | |               | |        | |
                | |_  | |_   _| |    _| |__   | |_/ /___| | ___   __ _  __| | ___  __| |
                |  _| | | | | | |   | | '_ \  |    // _ \ |/ _ \ / _` |/ _` |/ _ \/ _` |
                | |   | | |_| | |___| | |_) | | |\ \  __/ | (_) | (_| | (_| |  __/ (_| |
                \_|   |_|\__, \_____/_|_.__/  \_| \_\___|_|\___/ \__,_|\__,_|\___|\__,_|
                          __/ |                                                         
                         |___/
                ::FlyLib Reloaded | by @kotx__ | Inject successfully.::
                
                If you have any bugs, feature suggestions and questions, please contact us at https://github.com/TeamKun/flylib-reloaded.
                
                """.trimIndent()
            )
        } catch (e: Exception) {
            println()
            logger.error("Injection failed.")
            logger.error("Please contact https://github.com/TeamKun/flylib-reloaded/issues.")
            logger.error("with the following stacktrace and a description of how to reproduce the problem.")
            println()
            e.printStackTrace()
            println()
        }
    }

    private fun onLoad() {
        commandHandler.onLoad()
    }

    private fun onDisable() {
        try {
            logger.info("Disabling...")

            registeredListeners.forEach { k, v ->
                v.forEach {
                    unRegisterListener(k.java, it)
                }
            }

            commandHandler.onDisable()

            logger.info("Disable completed!")

        } catch (e: Exception) {
            println()
            logger.error("Unregister failed.")
            logger.error("Please contact https://github.com/TeamKun/flylib-reloaded/issues.")
            logger.error("with the following stacktrace and a description of how to reproduce the problem.")
            println()
            e.printStackTrace()
            println()
        }
    }

    inline fun <reified T : Event> registerListener(priority: EventPriority = EventPriority.NORMAL, action: Builder.ListenerAction<in T>): RegisteredListener {
        return registerListener(T::class.java, priority, action)
    }

    inline fun <reified T : Event> registerListener(clazz: Class<out T>, priority: EventPriority = EventPriority.NORMAL, action: Builder.ListenerAction<in T>): RegisteredListener {
        val plugin: JavaPlugin = FlyLibContext.get().get()
        val handlerList = clazz.kotlin::functions.get().find { it.name == "getHandlerList" }?.call() as HandlerList
        val registeredListener = RegisteredListener(
            object : Listener {},
            { _, event -> action.handle(event as T) },
            priority,
            plugin,
            false
        )
        handlerList.register(registeredListener)

        return registeredListener
    }

    inline fun <reified T : Event> unRegisterListener(listener: RegisteredListener) {
        unRegisterListener(T::class.java, listener)
    }

    inline fun <reified T : Event> unRegisterListener(clazz: Class<out T>, listener: RegisteredListener) {
        val handlerList = clazz.kotlin::functions.get().find { it.name == "getHandlerList" }?.call() as HandlerList
        handlerList.unregister(listener)
    }

    class Builder(
        private val plugin: JavaPlugin
    ) {
        private var commandHandler: CommandHandler = CommandHandler.Builder().build()
        private val handlers = mutableMapOf<KClass<out Event>, MutableList<Pair<ListenerAction<in Event>, EventPriority>>>()

        fun command(action: CommandHandler.Builder.Action): Builder {
            command(CommandHandler.Builder().apply {
                action.apply {
                    initialize()
                }
            }.build())
            return this
        }

        fun command(commandHandler: CommandHandler): Builder {
            this.commandHandler = commandHandler
            return this
        }

        inline fun <reified T : Event> listen(priority: EventPriority = EventPriority.NORMAL, action: ListenerAction<T>) {
            listen(T::class.java, action, priority)
        }

        @JvmOverloads
        fun <T : Event> listen(clazz: Class<T>, action: ListenerAction<T>, priority: EventPriority = EventPriority.NORMAL) {
            handlers.putIfAbsent(clazz.kotlin, mutableListOf())

            handlers[clazz.kotlin]!!.add(action as ListenerAction<in Event> to priority)
        }

        fun build(): FlyLib = FlyLib(
            plugin,
            commandHandler,
            handlers
        )

        fun interface BuilderAction {
            fun Builder.initialize()
        }

        fun interface ListenerAction<T : Event> {
            fun handle(event: T)
        }
    }

    companion object {
        @JvmStatic
        fun inject(plugin: JavaPlugin, action: Builder.BuilderAction): FlyLib = Builder(plugin).apply { action.apply { initialize() } }.build()
    }
}
fun JavaPlugin.flyLib(init: FlyLib.Builder.() -> Unit = {}) = FlyLib.inject(this, init)