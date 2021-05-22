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
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin

class TestPlugin : JavaPlugin() {
    override fun onEnable() {
        flyLib {
            command {
                register(TestCommand())
            }
        }
    }
}

class TestCommand : Command("test") {
    override val description: String = "Test command."
    override val usages: List<Usage> = listOf(
        Usage(
            Argument.Integer("number", 0, 20) {
                listOf(Suggestion("1"), Suggestion("10", "Example tooltip"))
            },
            Argument.Position("position")
        ) {
            sendMessage(args.joinToString(" "))
            val number = typedArgs[0] as Int
            val location = typedArgs[1] as Location
            sendMessage("num: $number")
            sendMessage("loc: $location")
        }
    )
}