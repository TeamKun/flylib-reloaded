/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.test_plugin

import dev.kotx.flylib.command.Command
import dev.kotx.flylib.flyLib
import org.bukkit.plugin.java.JavaPlugin

class TestPlugin : JavaPlugin() {
    init {
        flyLib {
            command()
        }
    }
}

class ExplodeCommand : Command("explode") {
    init {
        usage {
            entityArgument("targets")
            integerArgument("power", min = 1, max = 10)
            selectionArgument("mode", "")
        }
    }
}