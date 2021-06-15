/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib

import dev.kotx.flylib.command.*
import org.bukkit.plugin.java.JavaPlugin
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.slf4j.LoggerFactory
import java.util.function.*

class FlyLib(
    private val plugin: JavaPlugin,
    private val commandHandler: CommandHandler,
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

        /**
         * Configure the command module.
         * This is a method that corresponds to Kotlin's apply builder pattern.
         *
         * @param init CommandHandler.Builder's Configurations
         * @return FlyLib.Builder
         */
        fun command(action: CommandHandlerAction): Builder {
            commandHandler = CommandHandler.Builder().apply {
                action.apply {
                    initialize()
                }
            }.build()
            return this
        }

        /**
         * Build and initialize FlyLib.
         * It is recommended to call it with the onEnable clause of the JavaPlugin, since commands are registered at this time.
         */
        fun build(): FlyLib = FlyLib(
            plugin,
            commandHandler,
        )
    }

    companion object {
        @JvmStatic
        fun inject(plugin: JavaPlugin, action: FlyLibAction): FlyLib = Builder(plugin).apply { action.apply { initialize() } }.build()
    }
}

fun interface FlyLibAction {
    fun FlyLib.Builder.initialize()
}

/**
 * An extended formula for builders to build FlyLib.
 *
 * @param init FlyLib.Builder's Configurations
 * @return FlyLib Instance
 */
fun JavaPlugin.flyLib(init: FlyLib.Builder.() -> Unit = {}) = FlyLib.inject(this, init)