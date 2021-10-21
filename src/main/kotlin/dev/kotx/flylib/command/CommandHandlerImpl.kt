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

        println()
        println("\u001B[34m\u001B[1mCommands added:\u001B[m")
        commands.forEach { command ->
            val cmdArgument = getArgument(command.name, command)

            commandDispatcher.a().root.addChild(cmdArgument)
            commandNodes[command.name] = VanillaCommandWrapper(commandDispatcher, cmdArgument)

            command.aliases.forEach { alias ->
                val aliasArgument = getArgument(alias, command)
                commandDispatcher.a().root.addChild(aliasArgument)
                commandNodes[alias] = VanillaCommandWrapper(commandDispatcher, aliasArgument)
            }

            println("    \u001B[32m\u001B[1m[+]\u001B[m\u001B[32m ${command.name}\u001B[m")

//            log(
//                command.name, command.description ?: "no description available",
//                command.usages.joinToString("\n") { "/${command.name} ${it.arguments.joinToString(" ") { "<${it.name}>" }}" } + if (command.children.isNotEmpty())
//                    "\n${command.children.joinToString("\n") { "    ${it.name}" }}"
//                else
//                    ""
//            )
        }

        println()
        println("\u001B[34m\u001B[1mPermissions added:\u001B[m")

        val permissions = (commands.map { it.getCommandPermission() } +
                commands.flatMap { cmd -> cmd.usages.map { cmd.getUsagePermission(it) } }).distinct()
        permissions.forEach {
            flyLib.plugin.server.pluginManager.addPermission(BukkitPermission(it.first, it.second.defaultPermission))
            println("    \u001B[32m\u001B[1m[+]\u001B[m\u001B[32m ${it.first} (${it.second.defaultPermission.name})\u001B[m")
        }
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

        println()
        println("\u001B[34m\u001B[1mCommands removed:\u001B[m")

        commands.forEach { cmd ->
            remove(cmd.name)

            cmd.aliases.forEach {
                remove(it)
            }

            println("    \u001B[31m\u001B[1m[-]\u001B[m\u001B[31m ${cmd.name}\u001B[m")
        }

        println()
        println("\u001B[34m\u001B[1mPermissions removed:\u001B[m")

        commands.forEach { cmd ->
            flyLib.plugin.server.pluginManager.removePermission(cmd.getCommandPermission().first)
            println("    \u001B[31m\u001B[1m[-]\u001B[m\u001B[31m ${cmd.getCommandPermission().first} (${cmd.getCommandPermission().second.defaultPermission.name})\u001B[m")
            cmd.usages.forEach {
                flyLib.plugin.server.pluginManager.removePermission(cmd.getUsagePermission(it).first)
                println("    \u001B[31m\u001B[1m[-]\u001B[m\u001B[31m ${cmd.getUsagePermission(it).first} (${cmd.getUsagePermission(it).second.defaultPermission.name})\u001B[m")
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
        val commandArgument = LiteralArgumentBuilder.literal<CommandListenerWrapper>(name).apply {
            requires {
                it.bukkitSender.hasPermission(command.getCommandPermission().first)
            }

            executes { ctx ->
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
                    println("The following error occurred when executing the ${command.name} command.")
                    println("Context:")
                    println("\tSender     | ${context.sender.name}")
                    println("\tWorld      | ${context.world?.name}")
                    println("\tInput      | ${ctx.input}")
                    println("\tArguments  | ${context.args}")
                    println("\tTyped Args | ${context.typedArgs}")
                    println()
                    println("Stacktrace:")
                    e.printStackTrace()
                    println()

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
                        appendln()
                        boldln("Stacktrace:", Color.CYAN)
                        e.stackTraceToString().replace("\t", "    ").lines().forEach {
                            appendln(it)
                        }
                    }

                    return@executes 0
                }

                1
            }
        }

        command.usages.forEach { usage ->
            var usageArgument: ArgumentBuilder<CommandListenerWrapper, *>? = null
            usage.arguments.reversed().forEachIndexed { i, argument ->
                val argumentBuilder: ArgumentBuilder<CommandListenerWrapper, *> = if (argument.type == null)
                    LiteralArgumentBuilder.literal(argument.name)
                else
                    RequiredArgumentBuilder.argument(argument.name, argument.type)

                argumentBuilder.apply {
                    requires {
                        it.bukkitSender.hasPermission(command.getUsagePermission(usage).first)
                    }

                    if (i == 0) executes { ctx ->
                        val context = CommandContext(
                            flyLib.plugin,
                            command,
                            ctx.source.bukkitSender,
                            ctx.source.bukkitWorld,
                            ctx.source.server.server as Server,
                            ctx.input,
                            depthMap[command]!!,
                            usage.arguments.map {
                                try {
                                    it.parse(ctx, it.name)
                                } catch (e: Exception) {
                                    null
                                }
                            }
                        )

                        try {
                            usage.action?.apply { context.execute() } ?: command.apply { context.execute() }
                        } catch (e: Exception) {
                            println("The following error occurred when executing the ${command.name} command.")
                            println("Usage:")
                            println("\tDescription    | ${usage.description}")
                            println("\tPermission     | name: ${usage.permission?.name}, default: ${usage.permission?.defaultPermission}")
                            println("\tExpected Args  | ${usage.arguments.joinToString { "${it.name}(${it.type?.javaClass?.name})" }}")
                            println()
                            println("Context:")
                            println("\tSender         | ${context.sender.name}")
                            println("\tWorld          | ${context.world?.name}")
                            println("\tInput          | ${ctx.input}")
                            println("\tArguments      | ${context.args}")
                            println("\tTyped Args     | ${context.typedArgs}")
                            println()
                            println("Stacktrace:")
                            e.printStackTrace()
                            println()

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
                                appendln()
                                boldln("Stacktrace:", Color.CYAN)
                                e.stackTraceToString().replace("\t", "    ").lines().forEach {
                                    appendln(it)
                                }
                            }
                            return@executes 0
                        }

                        1
                    } else executes { ctx ->
                        val context = CommandContext(
                            flyLib.plugin,
                            command,
                            ctx.source.bukkitSender,
                            ctx.source.bukkitWorld,
                            ctx.source.server.server as Server,
                            ctx.input,
                            depthMap[command]!!,
                            usage.arguments.map {
                                try {
                                    it.parse(ctx, it.name)
                                } catch (e: Exception) {
                                    null
                                }
                            }
                        )

                        context.sendHelp()
                        1
                    }

                    if (this is RequiredArgumentBuilder<CommandListenerWrapper, *> && argument.suggestion != null)
                        suggests { context, builder ->
                            val suggestions = SuggestionBuilder(
                                flyLib.plugin,
                                command,
                                context.source.bukkitSender,
                                context.source.bukkitWorld,
                                context.source.server.server as Server,
                                context.input,
                                depthMap[command]!!,
                                usage.arguments.map {
                                    try {
                                        it.parse(context, it.name)
                                    } catch (e: Exception) {
                                        null
                                    }
                                }
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
                }

                if (usageArgument != null)
                    argumentBuilder.then(usageArgument)

                usageArgument = argumentBuilder
            }



            if (usageArgument != null)
                commandArgument.then(usageArgument)
        }

        command.children.forEach {
            commandArgument.then(getArgument(it.name, it))
        }

        return commandArgument.build()
    }

    private fun Command.getCommandPermission() =
        ((permission ?: defaultPermission).name
            ?: (flyLib.plugin.name.lowercase() +
                    ".command" +
                    ".${fullCommand.joinToString(".") { it.name }.lowercase()}")) to
                (permission ?: defaultPermission)

    private fun Command.getUsagePermission(usage: Usage) =
        ((usage.permission ?: permission ?: defaultPermission).name
            ?: (flyLib.plugin.name.lowercase() +
                    ".command" +
                    ".${fullCommand.joinToString(".") { it.name }.lowercase()}" +
                    ".${usage.arguments.joinToString(".") { it.name }.lowercase()}")) to
                (usage.permission ?: permission ?: defaultPermission)

    private fun List<Command>.handle(d: Int) {
        val children = flatMap {
            depthMap[it] = d
            it.children
        }

        if (children.isNotEmpty())
            children.handle(d + 1)
    }

    private fun log(headerTitle: String, headerBody: String, body: String) {
        println("\u001B[32m┌──────────────────────────────────────────────────────────────┐\u001B[m")
        println("\u001B[32m| \u001B[1m$headerTitle - \u001B[m\u001B[32m$headerBody${" ".repeat(60 - (headerTitle.length + headerBody.length + 3))} |\u001B[m")
        println("\u001B[32m├--------------------------------------------------------------┤\u001B[m")
        println(body.split("\n").joinToString("\n") { "\u001B[32m| $it${" ".repeat(60 - it.length)} |\u001B[m" })
        println("\u001B[32m└──────────────────────────────────────────────────────────────┘\u001B[m")
    }
}