/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.test

import dev.kotx.flylib.*
import dev.kotx.flylib.command.*
import dev.kotx.flylib.command.internal.*
import org.bukkit.plugin.java.*

class TestPlugin : JavaPlugin() {
    override fun onEnable() {
        flyLib {
            command {
                defaultConfiguration {
                    permission(Permission.OP)
                }

                register(PrintNumberCommand)
                register(TabCompleteCommand)
                register(ParentCommand)
            }
        }
    }
}

object PrintNumberCommand : Command("printnumber") {
    override val usages: List<Usage> = listOf(
        Usage(
            Argument.Integer("number", min = 0, max = 10)
        ) {
            sendMessage("You sent ${args.first()}!")
        }
    )
}

object TabCompleteCommand : Command("tabcomplete") {
    override val usages: List<Usage> = listOf(
        Usage(
            Argument.Selection("mode", "active", "inactive"),
            Argument.Player("target"),
            Argument.Position("position")
        )
    )
}

object ParentCommand : Command("parent") {
    override val children: List<Command> = listOf(ChildrenCommand)

    object ChildrenCommand: Command("children") {
        override fun CommandContext.execute() {
            sendMessage("You executed children command!")
        }
    }
}