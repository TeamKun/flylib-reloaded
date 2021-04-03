package kotx.minecraft.testserver

import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandConsumer
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
    override val usages: List<Usage> = listOf(
        Usage("test <aaa/bbb/ccc> <user> <arg1> [..")
    )

    override fun CommandConsumer.execute() {
        sendHelp()
    }
}