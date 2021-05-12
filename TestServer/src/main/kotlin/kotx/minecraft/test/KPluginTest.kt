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
                }
            }
        }
    }
}

class MainCommand : Command("main") {
    override val usages: List<Usage> = listOf(
        Usage(
            Argument.Text("text"),
            Argument.Entity("user"),
            Argument.Text("input"),
            Argument.Selection("mode", "hoge", "fuga"),
        )
    )

    override fun CommandContext.execute() {
        sendHelp()
    }
}