/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.test_plugin

import dev.kotx.flylib.command.Command
import dev.kotx.flylib.command.CommandContext
import dev.kotx.flylib.flyLib
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.plugin.java.JavaPlugin

class TestPlugin : JavaPlugin() {
    init {
        flyLib {
            command(TestCommand())

            listen(PlayerMoveEvent::class.java) { event ->
                println("MOV")
            }
        }
    }
}

class TestCommand : Command("test") {
    init {
        usage {
            integerArgument("number")
        }
    }

    override fun CommandContext.execute() {
        pluginMessage("Your content -> ${args.first()}")
    }
}