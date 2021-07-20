/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.dev.test

import dev.kotx.flylib.command.Command
import dev.kotx.flylib.command.CommandContext
import dev.kotx.flylib.command.Permission.Companion.EVERYONE
import dev.kotx.flylib.flyLib
import org.bukkit.plugin.java.JavaPlugin

class TestPlugin : JavaPlugin() {
    init {
        flyLib {
            command(TestCommand, ChildCommand)
        }
    }
}

object TestCommand : Command("test") {
    init {
        description("Hello flylib!")
        permission(EVERYONE)
        alias("hoge")
        usage {
            description("some description")
            integerArgument("number")
            entityArgument("entity")
        }
        usage {
            description("second usage")
            locationArgument("location")
        }
        example("/test 1 kotlinx")
        example("test ~ ~ ~")
        children(ChildCommand)
    }
}

object ChildCommand : Command("children") {
    init {
        usage {
            description("children usage")
            locationArgument("location")
        }
    }

    override fun CommandContext.execute() {
        sender.sendMessage("Hello children $args")
    }
}
