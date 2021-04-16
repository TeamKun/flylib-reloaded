/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.test

import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandContext
import kotx.minecraft.libs.flylib.command.complete.providers.BasicCompletionContributor
import kotx.minecraft.libs.flylib.command.complete.providers.ChildrenCompletionContributor
import kotx.minecraft.libs.flylib.command.complete.providers.OptionCompletionContributor
import kotx.minecraft.libs.flylib.command.complete.providers.UsageCompletionContributor
import kotx.minecraft.libs.flylib.command.internal.Permission
import kotx.minecraft.libs.flylib.flyLib
import org.bukkit.plugin.java.JavaPlugin

class PluginTest : JavaPlugin() {
    private val flyLib = flyLib {
        command {
            register(TestCommand())

            default {
                description("this is a description of the default command.")
                permission(Permission.EVERYONE)
                invalidMessage { "Hey! Looks like you don't have the necessary permissions to run the command!" }
            }

            completion {
                register(
                    ChildrenCompletionContributor(),
                    OptionCompletionContributor(),
                    UsageCompletionContributor(),
                    BasicCompletionContributor()
                )
            }
        }
    }

    override fun onEnable() {
        flyLib.initialize()
    }
}

class TestCommand : Command("test") {
    override fun CommandContext.execute() {
        if (args.isEmpty()) {
            sendHelp()
            return
        }

        sendMessage("Hello ${args.first()}!")
    }
}