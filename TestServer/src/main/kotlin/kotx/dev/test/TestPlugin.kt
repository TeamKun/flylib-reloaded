/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.dev.test

import dev.kotx.flylib.*
import dev.kotx.flylib.command.*
import dev.kotx.flylib.command.Permission.Companion.EVERYONE
import org.bukkit.plugin.java.*

class TestPlugin : JavaPlugin() {
    val flyLib = FlyLib.create(this, listOf(TestCommand()))
}

class TestCommand : Command("test") {
    init {
        description("Hello flylib!")
        permission(EVERYONE)
        usage {
            textArgument("YOUR_TEXT")
            executes {
                it.sender.sendMessage("Hello argument ${it.args}")
            }
        }

        children(ChildCommand())
    }

    override fun CommandContext.execute() {
        sender.sendMessage("Hello test $args")
    }
}

class ChildCommand : Command("children") {
    override fun CommandContext.execute() {
        sender.sendMessage("Hello children $args")
    }
}
