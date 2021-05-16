/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.test

import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.internal.Argument
import kotx.minecraft.libs.flylib.command.internal.Suggestion
import kotx.minecraft.libs.flylib.command.internal.Usage
import kotx.minecraft.libs.flylib.flyLib
import org.bukkit.plugin.java.JavaPlugin

class PluginTest : JavaPlugin() {
    override fun onEnable() {
        flyLib {
            command {
                defaultConfiguration {
                    playerOnly(true)
                }

                register(PrintNumberCommand())
                register(ExplodeCommand())
                register(OuterCommand())
            }
        }
    }
}

class PrintNumberCommand : Command("printnumber") {
    override val usages: List<Usage> = listOf(
        Usage(Argument.Integer("number", max = 10)) {
            sendMessage("Your Number -> ${args.first().toInt()}")
        }
    )
}

class ExplodeCommand : Command("explode") {
    override val usages: List<Usage> = listOf(
        Usage(
            Argument.Selection("type", "here", "there")
        ),
        Usage(
            Argument.Position("location")
        ),
        Usage(
            Argument.Player("player")
        ),
    )
}

class OuterCommand : Command("outer") {
    override val children: List<Command> = listOf(
        InnerCommand()
    )

    class InnerCommand : Command("inner") {
        override val usages: List<Usage> = listOf(
            Usage(
                Argument.Text("text") {
                    listOf(Suggestion("hoge", "hoge tooltip"), Suggestion("fuga", "fuga tooltip!"))
                }
            )
        )
    }
}