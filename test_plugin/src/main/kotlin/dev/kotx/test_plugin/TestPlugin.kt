/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.test_plugin

import dev.kotx.flylib.command.Command
import dev.kotx.flylib.command.CommandContext
import dev.kotx.flylib.flyLib
import org.bukkit.plugin.java.JavaPlugin

class TestPlugin : JavaPlugin() {
    init {
        flyLib {
            command(TestCommand())
        }
    }
}

class TestCommand : Command("test") {
    init {
        usage {
            textArgument("content")
        }
    }

    override fun CommandContext.execute() {
        pluginMessage("Your content -> ${args.first()}")
    }
}