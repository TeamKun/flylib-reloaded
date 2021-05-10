/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.test

import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandContext
import kotx.minecraft.libs.flylib.command.internal.Argument
import kotx.minecraft.libs.flylib.command.internal.Permission
import kotx.minecraft.libs.flylib.command.internal.Usage
import kotx.minecraft.libs.flylib.flyLib
import org.bukkit.plugin.java.JavaPlugin

class PluginTest : JavaPlugin() {
    override fun onEnable() {
        flyLib {
            command {
                register(MainCommand())
                defaultConfiguration {
                    description("this is a description of the default command.")
                    permission(Permission.EVERYONE)
                    invalidMessage { "Hey! Looks like you don't have the necessary permissions to run the command!" }
                }
            }
        }
    }
}

class MainCommand : Command("main") {
    override val children: List<Command> = listOf(
        MainSub1Command(),
        MainSub2Command()
    )

    override fun CommandContext.execute() {
        sendHelp()
    }
}

class MainSub1Command : Command("sub1") {
    override val usages: List<Usage> = listOf(
        Usage(
            Argument.Text("sub1_txt")
        )
    )

    override fun CommandContext.execute() {
        sendMessage("You executed sub1 command.")
    }
}

class MainSub2Command : Command("sub2") {
    override val usages: List<Usage> = listOf(
        Usage(
            Argument.Integer("sub2_int")
        )
    )

    override fun CommandContext.execute() {
        sendMessage("You executed sub2 command.")
    }
}