/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotx.minecraft.libs.flylib.get
import kotx.minecraft.libs.flylib.send
import net.md_5.bungee.api.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory

abstract class Command(
    val name: String
) : CoroutineScope, KoinComponent {
    override val coroutineContext = Dispatchers.Default
    protected val logger = LoggerFactory.getLogger(this::class.java)!!
    protected val plugin by inject<JavaPlugin>()
    private val commandHandler by inject<CommandHandler>()

    /**
     * Command description. It is automatically displayed as a description when you execute sendHelp() from within the CommandConsumer.
     * By default, it is set to blank
     * You can also write the description over multiple lines. In that case, it will be automatically formatted by sendHelp().
     */
    open val description: String = ""

    /**
     * Command alias. A list of strings that can be used as abbreviations instead of using the official name of the command.
     */
    open val aliases: List<String> = listOf()

    /**
     * How to use the command. It is automatically formatted and displayed when you use sendHelp() in CommandConsumer.
     */
    open val usages: List<Usage> = listOf()

    /**
     * Command usage example. It is displayed in a list when you use sendHelp() in CommandConsumer.
     */
    open val examples: List<String> = listOf()

    /**
     * Permission to use the command. Permission.OP can be used only by OP, Permission.NOT_OP can be used by everyone except OP, and Permission.EVERYONE can be used by everyone.
     * By default, Permission.OP is specified.
     */
    open val permission: Permission = Permission.OP

    /**
     * A subcommand of this command. If the string entered as an argument matches the name or alias of these commands, the matching command will be executed.
     * This command will only be executed if there is no match.
     *
     * ### Example
     * Consider the case where "blacklist" is specified in the name of this command (eg. BlackListCommand) and a command named "add" (eg. AddCommand) has been added to children like below.
     * ```
     * children = listOf(AddCommand())
     * ```
     * If the user executes
     * ```
     * /blacklist add bbb ccc <..
     * ```
     * the AddCommand execute() will be executed. Placed in args from "bbb".
     *
     * If the user executes
     * ```
     * /blacklist aaa bbb ccc <..
     * ```
     * BlackListCommand will be executed. Placed in args from "aaa".
     */
    open val children = listOf<Command>()

    /**
     * This is the parent command for this command.
     * All commands added to children will automatically have parent specified during the command addition process.
     * This should not be specified explicitly by the developer.
     */
    var parent: Command? = null

    private fun validate(sender: CommandSender): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Only the player can execute this command!")
            return true
        }

        val canExecute = (permission == Permission.OP && sender.isOp)
                || (permission == Permission.NOT_OP && !sender.isOp)
                || permission == Permission.EVERYONE

        if (!canExecute) {
            sender.sendMessage("You can't execute this command!")
            return true
        }
        return false
    }

    fun handleExecute(sender: CommandSender, label: String, args: Array<out String>) {
        if (validate(sender)) return
        val consumer = CommandConsumer(
            sender as Player,
            sender.server,
            "$label ${args.joinToString(" ")}",
            args.toList().toTypedArray()
        )

        children[args.firstOrNull() ?: ""]?.handleExecute(sender, label, args.drop(1).toTypedArray())
            ?: consumer.execute()
    }

    fun handleTabComplete(
        sender: CommandSender,
        alias: String,
        args: Array<out String>
    ): List<String> {
        if (validate(sender)) return emptyList()
        val consumer = CommandConsumer(
            sender as Player,
            sender.server,
            "$alias ${args.joinToString(" ")}",
            args.toList().toTypedArray()
        )

        val r = children[args.firstOrNull() ?: ""]?.handleTabComplete(sender, alias, args.drop(1).toTypedArray())
        if (r == null) {
            val result = mutableListOf<String>()
            if (!commandHandler.disableAutoTabCompletion)
                result.addAll(children.map {
                    it.name
                } + usages.mapNotNull {
                    it.context.split(" ").getOrNull(args.size)
                }.flatMap {
                    val templateReg1 = "<(.+?)>".toRegex()
                    val templateReg2 = "\\[(.+?)]".toRegex()
                    val templateReg3 = "\\((.+?)\\)".toRegex()

                    val templateResult = templateReg1.find(it) ?: templateReg2.find(it) ?: templateReg3.find(it)
                    val templateContentSplit = templateResult?.groupValues?.get(1)?.split("[/|]".toRegex())

                    when {
                        it.startsWith("<..") || it.startsWith("[..") || it.startsWith("(..") -> listOf("...")
                        templateContentSplit?.size ?: 0 == 1 -> {
                            val replacements =
                                commandHandler.usageReplacementTemplates.filter { it.key(templateResult!!.groupValues[1]) }
                                    .flatMap { it.value.invoke(consumer) }
                            if (replacements.isEmpty()) listOf(it) else replacements
                        }
                        templateContentSplit?.size ?: 0 > 1 -> templateContentSplit!!.flatMap { s ->
                            val replacements = commandHandler.usageReplacementTemplates.filter { it.key(s) }
                                .flatMap { it.value.invoke(consumer) }
                            if (replacements.isNotEmpty()) replacements else listOf(s)
                        }
                        else -> listOf(it)
                    }
                })

            result.addAll(consumer.tabComplete())
            return result
        }

        return r
    }

    /**
     * Called when this command is executed. Since FlyLib has completed the authority check etc., please describe only the execution part.
     * Of course, this is not the case if you implement your own permission check.
     */
    protected abstract fun CommandConsumer.execute()

    /**
     * Where to implement tab completion. FlyLib automatically sorts and selects according to the user's input, so you only need to provide the list according to the size of the argument.
     * To disable the FlyLib auto tab selection feature, set disableAutoTabSelection to true in the injectFlyLib in commandHandler.
     */
    protected open fun CommandConsumer.tabComplete(): List<String> = emptyList()

    fun CommandConsumer.sendHelp() {
        var fullName = name
        fun Command.getFullName() {
            if (parent != null) {
                fullName = "${parent!!.name} $fullName"
                parent!!.getFullName()
            }
        }
        getFullName()

        val allUsages = mutableMapOf(
            this@Command to usages
        )
        val allExamples = mutableListOf<String>()

        fun List<Command>.handleChildren() {
            forEach {
                allUsages[it] = it.usages
                allExamples.addAll(it.examples)
                it.children.handleChildren()
            }
        }

        children.handleChildren()

        player.send {
            append("-----------------------------------\n").color(ChatColor.GRAY)
            append("Information of ").color(ChatColor.WHITE)
            append("/$fullName\n").color(ChatColor.GREEN)
            if (description.isNotBlank()) {
                append("Description: ").color(ChatColor.GRAY)
                append(description + "\n").color(ChatColor.WHITE)
            }
            if (aliases.isNotEmpty()) {
                append("Aliases: ").color(ChatColor.GRAY)
                append(aliases.joinToString(" ") + "\n").color(ChatColor.WHITE)
            }
            if (allUsages.isNotEmpty()) {
                append("Usages:\n").color(ChatColor.GRAY)
                allUsages.forEach { cmd, usages ->
                    usages.forEach {
                        fun Command.handleUsages(current: String): String = if (parent != null)
                            parent!!.handleUsages("${parent!!.name} $current")
                        else
                            "/$current"

                        append(cmd.handleUsages(it.context)).color(ChatColor.WHITE)
                        if (it.description.isNotBlank())
                            append(": ").append(it.description).color(ChatColor.GRAY)

                        append("\n")
                        val aliasMap = it.options.map {
                            it.aliases.joinToString(", ") {
                                if (it.length == 1)
                                    "-$it"
                                else
                                    "--$it"
                            }
                        }

                        val nameMap = it.options.map {
                            if (it.name.length == 1)
                                "-${it.name}"
                            else
                                "--${it.name}"
                        }

                        it.options.forEach {
                            append("    ")
                            if (it.aliases.isNotEmpty()) {
                                val len = aliasMap.maxByOrNull { it.length }?.length ?: 0
                                val aliases = it.aliases.joinToString(", ") {
                                    if (it.length == 1)
                                        "-$it"
                                    else
                                        "--$it"
                                }
                                append(aliases)
                                repeat(len - aliases.length) {
                                    append(" ")
                                }
                                append(", ")
                            }

                            val len = nameMap.maxByOrNull { it.length }?.length ?: 0
                            val name = if (it.name.length == 1)
                                "-${it.name}"
                            else
                                "--${it.name}"
                            append(name)
                            repeat(len - name.length) {
                                append(" ")
                            }
                            if (it.description.isNotBlank()) {
                                append(": ")
                                append(it.description)
                            }
                            if (it.required)
                                append(" (required)")
                            append("\n")
                        }
                        append("\n")
                    }
                }
            }
            if (allExamples.isNotEmpty()) {
                append("Examples:\n").color(ChatColor.GRAY)
                allExamples.forEach {
                    append("/$it")
                    append("\n")
                }
            }
            append("-----------------------------------").color(ChatColor.GRAY)
        }
    }
}