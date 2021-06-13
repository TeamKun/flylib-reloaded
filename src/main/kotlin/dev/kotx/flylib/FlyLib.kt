/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib

import dev.kotx.flylib.command.CommandHandler
import org.bukkit.plugin.java.JavaPlugin
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.slf4j.LoggerFactory

/**
 * This class is used to load the modules required for FlyLib.
 * FlyLib.Builder, or call the flylib method in a class that inherits from JavaPlugin.
 */
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
                
                If you find any bugs, please contact us at https://github.com/TeamKun/flylib-reloaded/issues.
                
                """.trimIndent()
            )
        } catch (e: Exception) {
            logger.error("Injection failed.")
            logger.error("Please contact https://github.com/TeamKun/flylib-reloaded/issues.")
            logger.error("with the following log and a description of how to reproduce the problem.")
            println()
            e.printStackTrace()
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
        fun command(init: CommandHandler.Builder.() -> Unit): Builder {
            commandHandler = CommandHandler.Builder().apply(init).build()
            return this
        }

        /**
         * Configure the command module.
         *
         * @param commandHandler CommandHandler's Instance
         * @return FlyLib.Builder
         */
        fun command(commandHandler: CommandHandler): Builder {
            this.commandHandler = commandHandler
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
}

/**
 * An extended formula for builders to build FlyLib.
 *
 * @param init FlyLib.Builder's Configurations
 * @return FlyLib Instance
 */
fun JavaPlugin.flyLib(init: FlyLib.Builder.() -> Unit = {}) = FlyLib.Builder(this).apply(init).build()