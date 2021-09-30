/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.test_plugin

import dev.kotx.flylib.command.Command
import dev.kotx.flylib.flyLib
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.plugin.java.JavaPlugin

class TestPlugin2 : JavaPlugin() {
    init {
        flyLib {
            command(Explode2Command())
            listen(BlockBreakEvent::class.java) { event ->
                //block break event logic
            }
        }
    }
}

class Explode2Command : Command("explode") {
    init {
        usage {
            entityArgument("targets")
            integerArgument("power", min = 1, max = 10)
            selectionArgument("mode", "one", "two")

            executes {
                message("You executed explode command!")
            }
        }
    }
}