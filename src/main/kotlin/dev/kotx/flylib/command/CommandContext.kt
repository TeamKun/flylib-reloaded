/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command

import dev.kotx.flylib.*
import net.kyori.adventure.text.*
import org.bukkit.*
import org.bukkit.command.*
import org.bukkit.entity.*
import org.bukkit.plugin.java.*

/**
 * By implementing Command execute and tabComplete as extended functions of CommandContext rather than as arguments,
 * you can avoid writing the same arguments to functions, and furthermore, you can aggregate functions that you want to be executed only from that command,
 * which improves the visibility.
 */
class CommandContext(
    val command: Command,
    val plugin: JavaPlugin,
    val sender: CommandSender,
    val message: String,
    val args: Array<String>,
    val typedArgs: Array<Any?>
) {

    val player: Player? = sender as? Player
    val server: Server? = player?.server
    val world: World? = player?.world

    val options = getOptions()
    val argsWithoutOptions = withoutOptionList()

    @JvmName("getOptions1")
    private fun getOptions(): Map<String, List<String>> {
        val singleHyphenRegex = "-([a-zA-Z0-9]+)".toRegex()
        val doubleHyphenRegex = "--([a-zA-Z0-9]+)".toRegex()

        val groups = mutableMapOf<String, List<String>>()
        val groupCache = mutableListOf<String>()
        args.reversed().forEach {

            when {
                it.matches(singleHyphenRegex) -> {
                    singleHyphenRegex.find(it)!!.groupValues[1].map(Char::toString).forEach {
                        groups[it] = emptyList()
                    }

                    groupCache.clear()
                }

                it.matches(doubleHyphenRegex) -> {
                    groups[doubleHyphenRegex.find(it)!!.groupValues[1]] = groupCache.toList().reversed()
                    groupCache.clear()
                }

                else -> groupCache.add(it)
            }
        }

        return groups
    }

    private fun withoutOptionList(): List<String> {
        val messages = mutableListOf<String>()
        args.forEach {
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