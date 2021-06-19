/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib

import dev.kotx.flylib.command.*
import org.bukkit.event.*
import org.bukkit.plugin.*
import org.bukkit.plugin.java.*
import org.koin.dsl.*
import org.slf4j.*
import kotlin.reflect.*
import kotlin.reflect.full.*

class FlyLib(
    private val plugin: JavaPlugin,
    private val commandHandler: CommandHandler,
    handlers: Map<KClass<out Event>, List<Builder.ListenerAction<in Event>>>,
) {
    private val logger = LoggerFactory.getLogger("::FlyLib Reloaded::")!!

    init {
        try {
            logger.info("injection start.")
            koinApplication {
                modules(module {
                    single { this@FlyLib }
                    single { this@FlyLib.plugin }
                    single { this@FlyLib.commandHandler }
                    single { this@FlyLib.logger }
                })
            }.also {
                FlyLibContext.startKoin(FlyLibContext, it)
            }

            handlers.forEach { event, actions ->
                (event::functions.get().find { it.name == "getHandlerList" }?.call() as HandlerList).register(
                    RegisteredListener(
                        object : Listener {},
                        { _, event ->
                            actions.forEach {
                                it.handle(event)
                            }
                        },
                        EventPriority.NORMAL,
                        plugin,
                        false
                    )
                )
            }

            commandHandler.initialize()

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

    class Builder(
        private val plugin: JavaPlugin
    ) {
        private var commandHandler: CommandHandler = CommandHandler.Builder().build()
        private val handlers = mutableMapOf<KClass<out Event>, MutableList<ListenerAction<in Event>>>()

        /**
         * Configure the command module.
         * This is a method that corresponds to Kotlin's apply builder pattern.
         *
         * @param action CommandHandler.Builder's Configurations
         * @return FlyLib.Builder
         */
        fun command(action: CommandHandler.Builder.Action): Builder {
            commandHandler = CommandHandler.Builder().apply {
                action.apply {
                    initialize()
                }
            }.build()
            return this
        }

        inline fun <reified T : Event> listen(action: ListenerAction<T>) {
            listen(T::class.java, action)
        }

        fun <T : Event> listen(clazz: Class<T>, action: ListenerAction<T>) {
            handlers.putIfAbsent(clazz.kotlin, mutableListOf())

            handlers[clazz.kotlin]!!.add(action as ListenerAction<in Event>)
        }

        /**
         * Build and initialize FlyLib.
         * It is recommended to call it with the onEnable clause of the JavaPlugin, since commands are registered at this time.
         */
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

/**
 * An extended formula for builders to build FlyLib.
 *
 * @param init FlyLib.Builder's Configurations
 * @return FlyLib Instance
 */
fun JavaPlugin.flyLib(init: FlyLib.Builder.() -> Unit = {}) = FlyLib.inject(this, init)