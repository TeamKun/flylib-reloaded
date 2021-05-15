/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib

import kotx.minecraft.libs.flylib.command.CommandHandler
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.slf4j.LoggerFactory

class FlyLib(
    private val plugin: JavaPlugin,
    private val commandHandler: CommandHandler,
) {
    private val logger = LoggerFactory.getLogger("::FlyLib Reloaded::")!!

    init {
        try {
            logger.info("injection start.")
            startKoin {
                modules(module {
                    single { this@FlyLib }
                    single { this@FlyLib.plugin }
                    single { this@FlyLib.commandHandler }
                    single { this@FlyLib.logger }
                })
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
                        """.trimIndent()
            )
        } catch (e: Exception) {
            logger.error("injection failed.")
            e.printStackTrace()
        }
    }

    class Builder(
        private val plugin: JavaPlugin
    ) {
        private var commandHandler: CommandHandler = CommandHandler.Builder().build()
        private val listeningEvents: MutableList<Function<Unit>> = mutableListOf()

        fun command(init: CommandHandler.Builder.() -> Unit): Builder {
            commandHandler = CommandHandler.Builder().apply(init).build()
            return this
        }

        fun command(commandHandler: CommandHandler): Builder {
            this.commandHandler = commandHandler
            return this
        }

        fun build(): FlyLib = FlyLib(
            plugin,
            commandHandler,
        )
    }
}

fun JavaPlugin.flyLib(block: FlyLib.Builder.() -> Unit = {}) = FlyLib.Builder(this).apply(block).build()