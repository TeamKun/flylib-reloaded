/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command

import kotx.minecraft.libs.flylib.sendErrorMessage
import kotx.minecraft.libs.flylib.sendPluginMessage
import kotx.minecraft.libs.flylib.sendSuccessMessage
import kotx.minecraft.libs.flylib.sendWarnMessage
import net.kyori.adventure.text.TextComponent
import org.bukkit.Server
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * By implementing Command execute and tabComplete as extended functions of CommandContext rather than as arguments,
 * you can avoid writing the same arguments to functions, and furthermore, you can aggregate functions that you want to be executed only from that command,
 * which improves the visibility.
 */
class CommandContext(
    val command: Command,
    val plugin: JavaPlugin,
    val sender: CommandSender,
    val player: Player?,
    val server: Server,
    val message: String,
    val args: Array<String>
) {
    val options = args.getOptions()
    val withoutOptions = args.withoutOptionList()

    private fun Array<String>.getOptions(): Map<String, List<String>> {
        val groups = mutableMapOf<String, List<String>>()
        val groupCache = mutableListOf<String>()
        reversed().forEach {
            if (it.startsWith("-")) {
                groups[it] = groupCache.toList().reversed()
                groupCache.clear()
            } else {
                groupCache.add(it)
            }
        }

        return groups
    }

    private fun Array<String>.withoutOptionList(): List<String> {
        val messages = mutableListOf<String>()
        forEach {
            if (it.startsWith("-"))
                return messages
            else
                messages.add(it)
        }

        return messages
    }

    fun send(block: TextComponent.Builder.() -> Unit) {
        sender.sendPluginMessage(plugin, block)
    }

    fun sendMessage(text: String) {
        sender.sendPluginMessage(plugin, text)
    }

    fun sendErrorMessage(text: String) {
        sender.sendErrorMessage(plugin, text)
    }

    fun sendWarnMessage(text: String) {
        sender.sendWarnMessage(plugin, text)
    }

    fun sendSuccessMessage(text: String) {
        sender.sendSuccessMessage(plugin, text)
    }
}