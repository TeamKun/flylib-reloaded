/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.testserver

import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandContext
import kotx.minecraft.libs.flylib.command.internal.Option
import kotx.minecraft.libs.flylib.command.internal.Permission
import kotx.minecraft.libs.flylib.command.internal.Usage
import kotx.minecraft.libs.flylib.injectFlyLib
import org.bukkit.plugin.java.JavaPlugin

class TestPlugin : JavaPlugin() {
    val flyLib = injectFlyLib {
        commandHandler {
            registerCommand(TestCommand())
            addUsageReplacement("user") {
                server.onlinePlayers.mapNotNull { it.playerProfile.name }
            }
        }
    }
}

class TestCommand : Command("test") {
    override val permission: Permission = Permission.EVERYONE
    override val description: String = "A test command"
    override val aliases: List<String> = listOf("t")
    override val usages: List<Usage> = listOf(
        Usage(
            "test <aaa/bbb/ccc> <user> <arg> [..", options = listOf(
                Option("opt", "Option!!", aliases = listOf("o")),
                Option("tst", "test", aliases = listOf("t")),
                Option("hoge", "hogeeeeee", aliases = listOf("h", "hg")),
            )
        )
    )
    override val examples: List<String> = listOf("test bbb Kotlinx hoge fuga --opt -th")

    override fun CommandContext.execute() {
        sendHelp()
    }
}