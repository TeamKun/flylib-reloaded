/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command

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

class CommandHandler(
    private val commands: List<Command>,
    val autoTabSelect: Boolean,
    val autoTabCompletion: Boolean,
    val usageReplacements: Map<(String) -> Boolean, CommandConsumer.() -> List<String>>,
    val completionContributors: List<CompletionContributor>
) : KoinComponent {
    private val plugin: JavaPlugin by inject()
    private val logger: Logger by inject()

    fun initialize() {
        logger.info("Registering command handling permission")
        plugin.server.pluginManager.addPermission(
            org.bukkit.permissions.Permission(
                "flylib",
                "flylib command handling permission.",
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
                permission = "flylib"
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
        private val usageReplacements = mutableMapOf<(String) -> Boolean, CommandConsumer.() -> List<String>>()
        var completionContributors = listOf(
            ChildrenCompletionContributor(),
            UsageCompletionContributor(),
            OptionCompletionContributor()
        )

        fun disableAutoTabSelect() {
            autoTabSelect = false
        }

        fun disableAutoTabCompletion() {
            autoTabCompletion = false
        }

        fun addUsageReplacement(
            templatePredicateProvider: (String) -> Boolean,
            resultProvider: CommandConsumer.() -> List<String>
        ) {
            usageReplacements[templatePredicateProvider] = resultProvider
        }

        fun addUsageReplacement(vararg template: String, resultProvider: CommandConsumer.() -> List<String>) {
            usageReplacements[{ s ->
                arrayOf(*template).any { s.equals(it, true) }
            }] = resultProvider
        }

        fun registerCommand(command: Command) {
            command.children.setParent(command)
            commands.add(command)
        }

        private fun List<Command>.setParent(parent: Command): Unit = forEach {
            it.parent = parent
            it.children.setParent(it)
        }

        fun registerCommand(
            name: String,
            description: String = "The description does not exist.",
            aliases: List<String> = emptyList(),
            usages: List<Usage> = emptyList(),
            examples: List<String> = emptyList(),
            permission: Permission = Permission.OP,
            children: List<Command> = emptyList(),
            tabComplete: CommandConsumer.() -> List<String> = { emptyList() },
            execute: CommandConsumer.() -> Unit
        ) {
            object : Command(name) {
                override val description: String = description
                override val aliases: List<String> = aliases
                override val usages: List<Usage> = usages
                override val examples: List<String> = examples
                override val permission: Permission = permission
                override val children: List<Command> = children

                override fun CommandConsumer.tabComplete(): List<String> = tabComplete()

                override fun CommandConsumer.execute() {
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
            completionContributors
        )
    }
}