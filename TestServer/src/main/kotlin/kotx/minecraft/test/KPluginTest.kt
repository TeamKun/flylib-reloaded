/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.test

import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.internal.Argument
import kotx.minecraft.libs.flylib.command.internal.Permission
import kotx.minecraft.libs.flylib.command.internal.Usage
import kotx.minecraft.libs.flylib.flyLib
import org.bukkit.plugin.java.JavaPlugin

class PluginTest : JavaPlugin() {
    override fun onEnable() {
        flyLib {
            command {
                register(PrintNumberCommand())
                defaultConfiguration {
                    description("this is a description of the default command.")
                    permission(Permission.EVERYONE)
                    invalidMessage { "Hey! Looks like you don't have the necessary permissions to run the command!" }
                }
            }
        }
    }
}

class PrintNumberCommand : Command("printnumber") {
    override val usages: List<Usage> = listOf(
        Usage(
            Argument.Selection("mode", listOf("add", "remove", "clear"))
        )
    )
}