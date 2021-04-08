/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command

import kotx.minecraft.libs.flylib.appendText
import kotx.minecraft.libs.flylib.command.internal.Permission
import kotx.minecraft.libs.flylib.command.internal.Usage
import kotx.minecraft.libs.flylib.get
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.awt.Color

abstract class Command(
    val name: String
) : KoinComponent {
    private val plugin by inject<JavaPlugin>()
    private val commandHandler by inject<CommandHandler>()

    /**
     * Command description. It is automatically displayed as a description when you execute sendHelp() from within the CommandContext.
     * By default, it is set to blank
     * You can also write the description over multiple lines. In that case, it will be automatically formatted by sendHelp().
     */
    open val description: String = ""

    /**
     * Command alias. A list of strings that can be used as abbreviations instead of using the official name of the command.
     */
    open val aliases: List<String> = listOf()

    /**
     * How to use the command. It is automatically formatted and displayed when you use sendHelp() in CommandContext.
     */
    open val usages: List<Usage> = listOf()

    /**
     * Command usage example. It is displayed in a list when you use sendHelp() in CommandContext.
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
        val canExecute = (permission == Permission.OP && sender.isOp)
                || (permission == Permission.NOT_OP && !sender.isOp)
                || permission == Permission.EVERYONE

        if (!canExecute) {
            return true
        }
        return false
    }

    fun handleExecute(sender: CommandSender, label: String, args: Array<out String>) {
        if (validate(sender)) {
            sender.sendMessage("You can't execute this command!")
            return
        }

        val context = CommandContext(
            plugin,
            sender,
            sender as? Player,
            sender.server,
            "$label ${args.joinToString(" ")}",
            args.toList().toTypedArray()
        )

        children[args.firstOrNull() ?: ""]?.handleExecute(sender, label, args.drop(1).toTypedArray())
            ?: context.execute()
    }

    fun handleTabComplete(
        sender: CommandSender,
        alias: String,
        args: Array<out String>
    ): List<String> {
        if (validate(sender)) return emptyList()
        val context = CommandContext(
            plugin,
            sender,
            sender as? Player,
            sender.server,
            "$alias ${args.joinToString(" ")}",
            args.toList().toTypedArray()
        )

        val subCommand = children[args.firstOrNull() ?: ""]

        return if (subCommand == null) {
            var result = mutableListOf<String>()
            result.addAll(context.tabComplete())

            commandHandler.completionContributors.forEach {
                if (commandHandler.autoTabCompletion)
                    result.addAll(it.suggest(this, context))
                if (commandHandler.autoTabSelect)
                    result = it.postProcess(result, this, context).toMutableList()
            }

            return result
        } else {
            subCommand.handleTabComplete(sender, alias, args.drop(1).toTypedArray())
        }
    }

    /**
     * Called when this command is executed. Since FlyLib has completed the authority check etc., please describe only the execution part.
     * Of course, this is not the case if you implement your own permission check.
     */
    protected abstract fun CommandContext.execute()

    /**
     * Where to implement tab completion. FlyLib automatically sorts and selects according to the user's input, so you only need to provide the list according to the size of the argument.
     * To disable the FlyLib auto tab selection feature, set disableAutoTabSelection to true in the injectFlyLib in commandHandler.
     */
    protected open fun CommandContext.tabComplete(): List<String> = emptyList()

    /**
     * A method that sends command usage, aliases, examples, etc. to the user in the current context.
     * It is supposed to be executed within execute.
     */
    fun CommandContext.sendHelp() {
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

        send {
            /**
            ---------------------------------
            /help(h): Show all commands list and usages.
            Usage: /help - Show all commands list
            Usages:
            /help - Show all commands list
            /help <command> - Show usages of <command>.

            Examples:
            /help
            /help ban
            ----------------------------------
             */
            val textColor = Color.CYAN
            appendText("-----------------------------------\n", Color.DARK_GRAY)
            appendText("/$fullName", textColor)
            if (aliases.isNotEmpty()) {
                appendText("(", Color.WHITE)
                aliases.forEachIndexed { i, it ->
                    appendText(it, textColor)
                    if (i < aliases.size - 1)
                        appendText(", ", Color.WHITE)
                }
                appendText(")", Color.WHITE)
            }

            if (description.isNotEmpty()) {
                appendText(": ", Color.WHITE)
                appendText(description, textColor)
            }

            appendText("\n")

            fun Command.handleUsages(current: String): String = if (parent != null)
                parent!!.handleUsages("${parent!!.name} $current")
            else
                "/$current"

            when (allUsages.values.flatten().size) {
                0 -> {
                }
                1 -> {
                    val usage = allUsages.values.flatten().first()
                    val cmd = allUsages.keys.first()

                    appendText("Usage: ", Color.WHITE)
                    appendText(cmd.handleUsages(usage.context), textColor)
                    if (usage.description.isNotEmpty()) {
                        appendText(" - ", Color.WHITE)
                        appendText(usage.description, textColor)
                    }

                    appendText("\n")
                }
                else -> {
                    appendText("Usages:\n", Color.WHITE)
                    allUsages.flatMap { (cmd, usages) ->
                        usages.map { cmd.handleUsages(it.context) to it.description }
                    }.toMap().forEach { (usg, desc) ->
                        appendText(usg, textColor)
                        if (desc.isNotEmpty()) {
                            appendText(" - ", Color.WHITE)
                            appendText(desc, textColor)
                        }
                        appendText("\n")
                    }
                }
            }

            when (allExamples.size) {
                0 -> {

                }

                1 -> {
                    appendText("Example: ", Color.WHITE)
                    appendText("/${allExamples.first()}\n", textColor)
                }

                else -> {
                    appendText("Examples:\n", Color.WHITE)
                    allExamples.forEachIndexed { i, it ->
                        appendText("/$it", textColor)
                        if (i < allExamples.size)
                            appendText("\n")
                    }
                }
            }

            appendText("-----------------------------------", Color.DARK_GRAY)
        }
    }
}