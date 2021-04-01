/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.test

import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandConsumer
import kotx.minecraft.libs.flylib.command.internal.Option
import kotx.minecraft.libs.flylib.command.internal.Usage
import kotx.minecraft.libs.flylib.injectFlyLib
import org.bukkit.plugin.java.JavaPlugin

class TestServer : JavaPlugin() {
    val flyLib = injectFlyLib {
        commandHandler {
            registerCommand(TestCommand)
            addUsageReplacement("user") {
                server.onlinePlayers.map { it.name }
            }
        }
    }
}

object TestCommand : Command("test") {
    override val description: String = "a test command."
    override val usages: List<Usage> = listOf(
        Usage(
            "test <a/b/c> <user> <something> [..",
            options = listOf(
                Option(
                    "opt",
                    "test option",
                    listOf("o"),
                    true
                )
            )
        )
    )

    override fun CommandConsumer.execute() {
        sendHelp()
    }
}