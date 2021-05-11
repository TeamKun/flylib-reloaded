/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command

import kotx.minecraft.libs.flylib.command.internal.Permission
import kotx.minecraft.libs.flylib.command.internal.Usage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class Command(
    val name: String
) : KoinComponent {
    internal val plugin by inject<JavaPlugin>()
    private val commandHandler by inject<CommandHandler>()

    /**
     * Command description. It is automatically displayed as a description when you execute sendHelp() from within the CommandContext.
     * By default, it is set to blank
     * You can also write the description over multiple lines. In that case, it will be automatically formatted by sendHelp().
     */
    open val description: String by lazy { commandHandler.commandDefault.description }

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
    open val permission: Permission by lazy { commandHandler.commandDefault.permission }

    /**
     * Can only the player execute this command? By default it can also be run from the server console. (default: false)
     */
    open val playerOnly: Boolean by lazy { commandHandler.commandDefault.playerOnly }

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

    fun validate(sender: CommandSender): Boolean {
        val validPermission = when (permission) {
            Permission.OP -> sender.isOp
            Permission.NOT_OP -> !sender.isOp
            Permission.EVERYONE -> true
        }

        if (!validPermission) return false

        val validSender = !playerOnly || playerOnly && sender is Player

        if (!validSender) return false

        return true
    }

    /**
     * Called when this command is executed. Since FlyLib has completed the authority check etc., please describe only the execution part.
     * Of course, this is not the case if you implement your own permission check.
     */
    open fun CommandContext.execute() {
        sendHelp()
    }

    /**
     * A method that sends command usage, aliases, examples, etc. to the user in the current context.
     * It is supposed to be executed within execute.
     */
    fun CommandContext.sendHelp() {
        commandHandler.commandDefault.sendHelp(this)
    }

    override fun getKoin(): Koin {
        return super.getKoin()
    }
}