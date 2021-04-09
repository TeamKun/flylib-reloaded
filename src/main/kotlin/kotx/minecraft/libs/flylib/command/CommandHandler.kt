/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command

import kotx.minecraft.libs.flylib.appendText
import kotx.minecraft.libs.flylib.command.complete.CompletionContributor
import kotx.minecraft.libs.flylib.command.complete.providers.ChildrenCompletionContributor
import kotx.minecraft.libs.flylib.command.complete.providers.OptionCompletionContributor
import kotx.minecraft.libs.flylib.command.complete.providers.UsageCompletionContributor
import kotx.minecraft.libs.flylib.command.internal.Permission
import kotx.minecraft.libs.flylib.command.internal.Usage
import kotx.minecraft.libs.flylib.get
import org.bukkit.command.CommandSender
import org.bukkit.permissions.PermissionDefault
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.Logger
import java.awt.Color

class CommandHandler(
    private val commands: List<Command>,
    val autoTabSelect: Boolean,
    val autoTabCompletion: Boolean,
    val usageReplacements: Map<(String) -> Boolean, CommandContext.() -> List<String>>,
    val completionContributors: List<CompletionContributor>,
    val defaultDescription: String,
    val defaultPermission: Permission,
    val defaultPlayerOnly: Boolean,
    val invalidCommandMessage: (Command) -> String,
    val defaultSendHelp: CommandContext.() -> Unit
) : KoinComponent {
    private val plugin: JavaPlugin by inject()
    private val logger: Logger by inject()

    fun initialize() {
        logger.info("Registering command handling permission")
        plugin.server.pluginManager.addPermission(
            org.bukkit.permissions.Permission(
                "flylib.op",
                PermissionDefault.OP
            )
        )
        plugin.server.pluginManager.addPermission(
            org.bukkit.permissions.Permission(
                "flylib.notop",
                PermissionDefault.NOT_OP
            )
        )
        plugin.server.pluginManager.addPermission(
            org.bukkit.permissions.Permission(
                "flylib.everyone",
                PermissionDefault.TRUE
            )
        )
        logger.info("Registering commands: (${commands.size})")
        commands.forEach {
            plugin.server.commandMap.register("flylib", object : org.bukkit.command.Command(it.name) {
                override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
                    handleCommand(sender, label, args)
                    return true
                }

                override fun tabComplete(
                    sender: CommandSender,
                    alias: String,
                    args: Array<out String>
                ) = handleTabComplete(sender, alias, args).toMutableList()
            }.apply {
                label = it.name
                aliases = it.aliases
                description = it.description
                permission = when (it.permission) {
                    Permission.OP -> "flylib.op"
                    Permission.NOT_OP -> "flylib.notop"
                    Permission.EVERYONE -> "flylib.everyone"
                }
                permissionMessage = "You can't execute ${it.name} command! (permission required)"
            })
            logger.info("Registered command: ${it.name}")
        }
        logger.info("Finished registering commands.")
    }

    private fun handleCommand(
        sender: CommandSender,
        label: String,
        args: Array<out String>
    ) {
        val cmd = commands[label] ?: return
        cmd.handleExecute(sender, label, args)
    }

    private fun handleTabComplete(
        sender: CommandSender,
        alias: String,
        args: Array<out String>
    ): List<String> {
        val cmd = commands[alias] ?: return emptyList()

        return cmd.handleTabComplete(sender, alias, args)
    }

    class Builder {
        private val commands = mutableListOf<Command>()
        private var autoTabSelect = true
        private var autoTabCompletion = true
        private val usageReplacements = mutableMapOf<(String) -> Boolean, CommandContext.() -> List<String>>()

        /**
         * Description specified by default when nothing is specified when creating a Command instance
         */
        var defaultDescription: String = ""

        /**
         * Privileges specified by default if nothing is specified when creating a command instance
         */
        var defaultPermission: Permission = Permission.OP

        /**
         * Whether or not only the player specified by default can be executed if nothing is specified when creating a command instance
         */
        var defaultPlayerOnly: Boolean = false

        /**
         * Message sent when the command is unavailable
         */
        private var invalidCommandMessage: (Command) -> String = {
            "You can't execute ${it.name} command!"
        }

        /**
         * Contents of send Help used by default when not overridden
         */
        private var defaultSendHelp: CommandContext.() -> Unit = {
            var fullName = command.name
            fun Command.getFullName() {
                if (parent != null) {
                    fullName = "${parent!!.name} $fullName"
                    parent!!.getFullName()
                }
            }
            command.getFullName()

            val allUsages = mutableMapOf(
                command to command.usages
            )
            val allExamples = mutableListOf<String>()

            fun List<Command>.handleChildren() {
                forEach {
                    allUsages[it] = it.usages
                    allExamples.addAll(it.examples)
                    it.children.handleChildren()
                }
            }

            command.children.handleChildren()

            send {
                val textColor = Color.CYAN
                appendText("-----------------------------------\n", Color.DARK_GRAY)
                appendText("/$fullName", textColor)
                if (command.aliases.isNotEmpty()) {
                    appendText("(", Color.WHITE)
                    command.aliases.forEachIndexed { i, it ->
                        appendText(it, textColor)
                        if (i < command.aliases.size - 1)
                            appendText(", ", Color.WHITE)
                    }
                    appendText(")", Color.WHITE)
                }

                if (command.description.isNotEmpty()) {
                    appendText(": ", Color.WHITE)
                    appendText(command.description, textColor)
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

        /**
         * A list of classes that inherit from Completion Contributor, which supports tab completion.
         * The processing is executed in order from the top as follows.
         * If these parameters are valid, the corresponding methods will be executed in the following order.
         * If AContributor and BContributor are present:
         * The result is
         * 1. Add all the List<String> provided by AContributor#suggest, and
         * 2. Overwritten by the output of AContributor#postProcess, and
         * 3. If there is a BContributor: result will add all of the List<String> provided by AContributor#suggest, and will be overwritten by the output of AContributor#postProcess.
         * 4. All the List<String> provided by BContributor#suggest are added and overwritten in the output of BContributor#postProcess.
         */
        var completionContributors = listOf(
            ChildrenCompletionContributor(),
            UsageCompletionContributor(),
            OptionCompletionContributor()
        )

        /**
         * Disables automatic tab selection / sorting.
         */
        fun disableAutoTabSelect() {
            autoTabSelect = false
        }

        /**
         * Disables automatic tab completion.
         */
        fun disableAutoTabCompletion() {
            autoTabCompletion = false
        }

        /**
         * If Usage Completion Contributor is specified, specify the conversion parameters specified in usages in a format like <...>.
         */
        fun addUsageReplacement(
            templatePredicateProvider: (String) -> Boolean,
            resultProvider: CommandContext.() -> List<String>
        ) {
            usageReplacements[templatePredicateProvider] = resultProvider
        }

        /**
         * If Usage Completion Contributor is specified, specify the conversion parameters specified in usages in a format like <...>.
         */
        fun addUsageReplacement(vararg template: String, resultProvider: CommandContext.() -> List<String>) {
            usageReplacements[{ s ->
                arrayOf(*template).any { s.equals(it, true) }
            }] = resultProvider
        }

        /**
         * Registers the specified command. It is not necessary to register commands or permissions in plugin.yml.
         */
        fun registerCommand(command: Command) {
            command.children.setParent(command)
            commands.add(command)
        }

        /**
         * Message sent when the command is unavailable
         */
        fun invalidCommandMessage(provider: (Command) -> String) {
            invalidCommandMessage = provider
        }

        /**
         * Contents of send Help used by default when not overridden
         */
        fun defaultSendHelp(provider: CommandContext.() -> Unit) {
            defaultSendHelp = provider
        }

        private fun List<Command>.setParent(parent: Command): Unit = forEach {
            it.parent = parent
            it.children.setParent(it)
        }

        /**
         * Registers the specified command. It is not necessary to register commands or permissions in plugin.yml.
         */
        fun registerCommand(
            name: String,
            description: String = "The description does not exist.",
            aliases: List<String> = emptyList(),
            usages: List<Usage> = emptyList(),
            examples: List<String> = emptyList(),
            permission: Permission = Permission.OP,
            children: List<Command> = emptyList(),
            tabComplete: CommandContext.() -> List<String> = { emptyList() },
            execute: CommandContext.() -> Unit
        ) {
            object : Command(name) {
                override val description: String = description
                override val aliases: List<String> = aliases
                override val usages: List<Usage> = usages
                override val examples: List<String> = examples
                override val permission: Permission = permission
                override val children: List<Command> = children

                override fun CommandContext.tabComplete(): List<String> = tabComplete()

                override fun CommandContext.execute() {
                    execute()
                }
            }.also {
                registerCommand(it)
            }
        }

        fun build() = CommandHandler(
            commands,
            autoTabSelect,
            autoTabCompletion,
            usageReplacements,
            completionContributors,
            defaultDescription,
            defaultPermission,
            defaultPlayerOnly,
            invalidCommandMessage,
            defaultSendHelp
        )
    }
}