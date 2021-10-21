/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import dev.kotx.flylib.FlyLibImpl
import dev.kotx.flylib.util.BOLD
import dev.kotx.flylib.util.CYAN
import dev.kotx.flylib.util.GREEN
import dev.kotx.flylib.util.RED
import dev.kotx.flylib.util.RESET
import dev.kotx.flylib.util.fullCommand
import dev.kotx.flylib.util.message
import net.minecraft.server.v1_16_R3.CommandListenerWrapper
import net.minecraft.server.v1_16_R3.MinecraftServer
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.craftbukkit.v1_16_R3.CraftServer
import org.bukkit.craftbukkit.v1_16_R3.command.CraftCommandMap
import org.bukkit.craftbukkit.v1_16_R3.command.VanillaCommandWrapper
import org.bukkit.entity.Player
import java.awt.Color

private typealias BukkitPermission = org.bukkit.permissions.Permission

@Suppress("UNCHECKED_CAST")
internal class CommandHandlerImpl(
    override val flyLib: FlyLibImpl,
    private val commands: List<Command>,
    private val defaultPermission: Permission
) : CommandHandler {
    private val depthMap = mutableMapOf<Command, Int>()
    internal fun enable() {
        val commandDispatcher = ((Bukkit.getServer() as CraftServer).server as MinecraftServer).commandDispatcher
        val commandNodes = (Bukkit.getCommandMap() as CraftCommandMap).knownCommands
        commands.handle(1)

        println("  $CYAN${BOLD}Commands added:$RESET")
        commands.forEach { command ->
            val cmdArgument = getArgument(command.name, command)

            commandDispatcher.a().root.addChild(cmdArgument)
            commandNodes[command.name] = VanillaCommandWrapper(commandDispatcher, cmdArgument)

            command.aliases.forEach { alias ->
                val aliasArgument = getArgument(alias, command)
                commandDispatcher.a().root.addChild(aliasArgument)
                commandNodes[alias] = VanillaCommandWrapper(commandDispatcher, aliasArgument)
            }

            println("    $GREEN$BOLD[+]$RESET$GREEN ${command.name}$RESET")
        }

        println()
        println("  $CYAN${BOLD}Permissions added:$RESET")

        val permissions = (commands.map { it.getCommandPermission() } +
                commands.flatMap { cmd -> cmd.usages.map { cmd.getUsagePermission(it) } }).distinct()
        permissions.filter {
            flyLib.plugin.server.pluginManager.getPermission(it.first) == null
        }.forEach {
            flyLib.plugin.server.pluginManager.addPermission(BukkitPermission(it.first, it.second.defaultPermission))

            println("    $GREEN$BOLD[+]$RESET$GREEN ${it.first} (${it.second.defaultPermission.name})$RESET")
        }
        println()
    }

    internal fun disable() {
        val root = ((Bukkit.getServer() as CraftServer).server as MinecraftServer).commandDispatcher.a().root
        val commandNodes = (Bukkit.getCommandMap() as CraftCommandMap).knownCommands

        fun remove(name: String) {
            root.removeCommand(name)
            root.removeCommand("minecraft:$name")
            commandNodes.remove(name)
            commandNodes.remove("minecraft:$name")
        }

        println("  $CYAN${BOLD}Commands removed:$RESET")

        commands.forEach { cmd ->
            remove(cmd.name)
            cmd.aliases.forEach { remove(it) }

            println("    $RED$BOLD[-]$RESET$RED ${cmd.name}$RESET")
        }

        println()
        println("  $CYAN${BOLD}Permissions removed:$RESET")

        commands.forEach { cmd ->
            val cmdPermission = cmd.getCommandPermission()
            flyLib.plugin.server.pluginManager.removePermission(cmdPermission.first)
            println("    $RED$BOLD[-]$RESET$RED ${cmdPermission.first} (${cmdPermission.second.defaultPermission.name})$RESET")

            cmd.usages.forEach {
                val usagePermission = cmd.getUsagePermission(it)
                flyLib.plugin.server.pluginManager.removePermission(usagePermission.first)

                println("    $RED$BOLD[-]$RESET$RED ${usagePermission.first} (${usagePermission.second.defaultPermission.name})$RESET")
            }
        }
        println()
    }

    internal fun load() {
        fun registerPermission(name: String, command: Command) {
            val commandMap = flyLib.plugin.server.commandMap

            commandMap.getCommand(name)?.permission = command.getCommandPermission().first
            commandMap.getCommand("minecraft:$name")?.permission = command.getCommandPermission().first
        }

        commands.forEach { cmd ->
            registerPermission(cmd.name, cmd)

            cmd.aliases.forEach {
                registerPermission(it, cmd)
            }
        }
    }

    private fun getArgument(name: String, command: Command): LiteralCommandNode<CommandListenerWrapper> {
        val commandArgument = LiteralArgumentBuilder.literal<CommandListenerWrapper>(name)
        commandArgument.requires {
            it.bukkitSender.hasPermission(command.getCommandPermission().first)
        }

        commandArgument.executes { ctx ->
            val context = CommandContext(
                flyLib.plugin,
                command,
                ctx.source.bukkitSender,
                ctx.source.bukkitWorld,
                ctx.source.server.server as Server,
                ctx.input,
                depthMap[command]!!
            )

            try {
                command.apply { context.execute() }
            } catch (e: Exception) {
                e.printStackTrace()

                if (ctx.source.bukkitSender.isOp && ctx.source.bukkitSender is Player) ctx.source.bukkitSender.message {
                    boldln("The following error occurred when executing the ${command.name} command.", Color.RED)
                    boldln("Context:", Color.CYAN)
                    bold("    Sender: ")
                    appendln(context.sender.name)
                    bold("    World: ")
                    appendln(context.world?.name ?: "null")
                    bold("    Input: ")
                    appendln(ctx.input)
                    bold("    Arguments: ")
                    appendln(context.args.joinToString())
                    bold("    Typed Args: ")
                    appendln(context.typedArgs.joinToString { it.toString() })
                }
            }

            1
        }

        command.usages.forEach { usage ->
            var usageArgument: ArgumentBuilder<CommandListenerWrapper, *>? = null
            usage.arguments.reversed().forEachIndexed { i, argument ->
                val argumentBuilder: ArgumentBuilder<CommandListenerWrapper, *> = if (argument.type == null)
                    LiteralArgumentBuilder.literal(argument.name)
                else
                    RequiredArgumentBuilder.argument(argument.name, argument.type)

                argumentBuilder.requires {
                    it.bukkitSender.hasPermission(command.getUsagePermission(usage).first)
                }

                argumentBuilder.executes { ctx ->
                    val context = CommandContext(
                        flyLib.plugin,
                        command,
                        ctx.source.bukkitSender,
                        ctx.source.bukkitWorld,
                        ctx.source.server.server as Server,
                        ctx.input,
                        depthMap[command]!!,
                        usage.parseArguments(ctx)
                    )

                    try {
                        if (i == 0)
                            usage.action?.apply { context.execute() } ?: command.apply { context.execute() }
                        else
                            context.sendHelp()
                    } catch (e: Exception) {
                        e.printStackTrace()

                        if (ctx.source.bukkitSender.isOp && ctx.source.bukkitSender is Player) ctx.source.bukkitSender.message {
                            boldln(
                                "The following error occurred when executing the ${command.name} command.",
                                Color.RED
                            )
                            boldln("Usage: ", Color.CYAN)
                            bold("    Description: ")
                            appendln(usage.description ?: "null")
                            bold("    Permission: name: ")
                            append(usage.permission?.name ?: "null")
                            bold(", default: ")
                            appendln(usage.permission?.defaultPermission?.name ?: "null")
                            bold("    Expected Args: ")
                            appendln(usage.arguments.joinToString { "${it.name}(${it.type?.javaClass?.name})" })
                            appendln()
                            boldln("Context:", Color.CYAN)
                            bold("    Sender: ")
                            appendln(context.sender.name)
                            bold("    World: ")
                            appendln(context.world?.name ?: "null")
                            bold("    Input: ")
                            appendln(ctx.input)
                            bold("    Arguments: ")
                            appendln(context.args.joinToString())
                            bold("    Typed Args: ")
                            appendln(context.typedArgs.joinToString { it.toString() })
                        }
                    }

                    1
                }

                if (argumentBuilder is RequiredArgumentBuilder<CommandListenerWrapper, *> && argument.suggestion != null)
                    argumentBuilder.suggests { ctx, builder ->
                        val suggestions = SuggestionBuilder(
                            flyLib.plugin,
                            command,
                            ctx.source.bukkitSender,
                            ctx.source.bukkitWorld,
                            ctx.source.server.server as Server,
                            ctx.input,
                            depthMap[command]!!,
                            usage.parseArguments(ctx)
                        )

                        argument.suggestion!!.apply { suggestions.initialize() }

                        suggestions.build().forEach {
                            if (it.tooltip == null)
                                builder.suggest(it.content)
                            else
                                builder.suggest(it.content) { it.tooltip }
                        }

                        builder.buildFuture()
                    }

                usageArgument?.also { argumentBuilder.then(it) }
                usageArgument = argumentBuilder
            }

            usageArgument?.also { commandArgument.then(it) }
        }

        command.children.forEach {
            commandArgument.then(getArgument(it.name, it))
        }

        return commandArgument.build()
    }

    private fun Command.getCommandPermission(): Pair<String, Permission> {
        val definedPermission = permission ?: defaultPermission

        val pluginName = flyLib.plugin.name.lowercase()
        val commandName = fullCommand.joinToString(".") { it.name }.lowercase()

        val permissionName = definedPermission.name ?: "$pluginName.command.$commandName"

        return permissionName to definedPermission
    }

    private fun Command.getUsagePermission(usage: Usage): Pair<String, Permission> {
        val definedPermission = usage.permission ?: permission ?: defaultPermission

        val pluginName = flyLib.plugin.name.lowercase()
        val commandName = fullCommand.joinToString(".") { it.name }.lowercase()
        val usageName = usage.arguments.joinToString(".") { it.name }.lowercase()

        val permissionName = definedPermission.name ?: ("$pluginName.command.$commandName.$usageName")

        return permissionName to definedPermission
    }

    private fun Usage.parseArguments(ctx: com.mojang.brigadier.context.CommandContext<CommandListenerWrapper>) =
        arguments.map {
            try {
                it.parse(ctx, it.name)
            } catch (e: Exception) {
                null
            }
        }

    private fun List<Command>.handle(depth: Int) {
        val children = flatMap {
            depthMap[it] = depth
            it.children
        }

        if (children.isNotEmpty())
            children.handle(depth + 1)
    }
}