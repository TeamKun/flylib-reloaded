/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import dev.kotx.flylib.FlyLibImpl
import dev.kotx.flylib.command.arguments.StringArgument
import dev.kotx.flylib.command.elements.ArrayElement
import dev.kotx.flylib.command.elements.BooleanArrayElement
import dev.kotx.flylib.command.elements.BooleanElement
import dev.kotx.flylib.command.elements.DoubleArrayElement
import dev.kotx.flylib.command.elements.DoubleElement
import dev.kotx.flylib.command.elements.FloatArrayElement
import dev.kotx.flylib.command.elements.FloatElement
import dev.kotx.flylib.command.elements.IntegerArrayElement
import dev.kotx.flylib.command.elements.IntegerElement
import dev.kotx.flylib.command.elements.LongArrayElement
import dev.kotx.flylib.command.elements.LongElement
import dev.kotx.flylib.command.elements.ObjectElement
import dev.kotx.flylib.command.elements.StringArrayElement
import dev.kotx.flylib.command.elements.StringElement
import dev.kotx.flylib.util.BOLD
import dev.kotx.flylib.util.CYAN
import dev.kotx.flylib.util.ComponentBuilder
import dev.kotx.flylib.util.GREEN
import dev.kotx.flylib.util.RED
import dev.kotx.flylib.util.RESET
import dev.kotx.flylib.util.asJsonObject
import dev.kotx.flylib.util.component
import dev.kotx.flylib.util.fullCommand
import dev.kotx.flylib.util.getBooleanArrayOrNull
import dev.kotx.flylib.util.getBooleanOrNull
import dev.kotx.flylib.util.getDoubleArrayOrNull
import dev.kotx.flylib.util.getDoubleOrNull
import dev.kotx.flylib.util.getFloatArrayOrNull
import dev.kotx.flylib.util.getFloatOrNull
import dev.kotx.flylib.util.getIntArrayOrNull
import dev.kotx.flylib.util.getIntOrNull
import dev.kotx.flylib.util.getLongArrayOrNull
import dev.kotx.flylib.util.getLongOrNull
import dev.kotx.flylib.util.getObjectOrNull
import dev.kotx.flylib.util.getStringArrayOrNull
import dev.kotx.flylib.util.getStringOrNull
import dev.kotx.flylib.util.message
import kotlinx.serialization.json.JsonObject
import net.minecraft.server.v1_16_R3.CommandListenerWrapper
import net.minecraft.server.v1_16_R3.MinecraftServer
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.craftbukkit.v1_16_R3.CraftServer
import org.bukkit.craftbukkit.v1_16_R3.command.CraftCommandMap
import org.bukkit.craftbukkit.v1_16_R3.command.VanillaCommandWrapper
import org.bukkit.entity.Player
import java.awt.Color
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

private typealias BukkitPermission = org.bukkit.permissions.Permission

@Suppress("UNCHECKED_CAST")
internal class CommandHandlerImpl(
    override val flyLib: FlyLibImpl,
    commands: List<Command>,
    private val defaultPermission: Permission,
    private val config: Config?,
    private val configCommandName: String?
) : CommandHandler {
    private val commands = commands.toMutableList()
    private val depthMap = mutableMapOf<Command, Int>()
    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .create()

    internal fun enable() {
        if (config != null) {
            loadConfig()

            val baseCommand = commands.find { it.name.equals(configCommandName, true) }
                ?: object : Command(flyLib.plugin.name.lowercase().replace(" ", "")) {

                    init {
                        permission(Permission.OP)
                    }
                }

            baseCommand.children.find { it.name.equals("config", true) }
                ?: config.getConfigCommand("config").also { baseCommand.children.add(it) }

            commands.removeIf { it.name == baseCommand.name }
            commands.add(baseCommand)
        }

        val commandDispatcher = ((Bukkit.getServer() as CraftServer).server as MinecraftServer).commandDispatcher
        val commandNodes = (Bukkit.getCommandMap() as CraftCommandMap).knownCommands

        commands.handleChildren(1)
        commands.forEach {
            it.children.handleParent(it)
        }

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

        permissions.filter { flyLib.plugin.server.pluginManager.getPermission(it.first) == null }.forEach {
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
        val commandMap = flyLib.plugin.server.commandMap

        fun registerPermission(name: String, command: Command) {
            commandMap.getCommand(name)?.permission = command.getCommandPermission().first
            commandMap.getCommand("minecraft:$name")?.permission = command.getCommandPermission().first
        }

        commands.forEach { cmd ->
            registerPermission(cmd.name, cmd)
            cmd.aliases.forEach { registerPermission(it, cmd) }
        }
    }

    private fun getArgument(name: String, command: Command): LiteralCommandNode<CommandListenerWrapper> {
        val commandArgument = LiteralArgumentBuilder.literal<CommandListenerWrapper>(name)
        commandArgument.requires {
            it.bukkitSender.hasPermission(command.getCommandPermission().first)
        }.executes { ctx ->
            val context = getCommandContext(command, ctx)

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
                    val context = getCommandContext(command, ctx, usage)

                    try {
                        if (i == 0)
                            argument.action?.apply { context.execute() } ?: usage.action?.apply { context.execute() }
                            ?: command.apply { context.execute() }
                        else
                            argument.action?.apply { context.execute() } ?: context.sendHelp()
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

    private fun getCommandContext(
        command: Command,
        context: com.mojang.brigadier.context.CommandContext<CommandListenerWrapper>,
        usage: Usage? = null
    ) = CommandContext(
        flyLib.plugin,
        config,
        command,
        context.source.bukkitSender,
        context.source.bukkitWorld,
        context.source.server.server as Server,
        context.input,
        depthMap[command]!!,
        usage?.parseArguments(context) ?: emptyList()
    )

    private fun Command.getCommandPermission(): Pair<String, Permission> {
        val definedPermission = permission ?: defaultPermission

        val pluginName = flyLib.plugin.name.lowercase().replace(" ", "_")
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

    private fun List<Command>.handleChildren(depth: Int) {
        val children = flatMap {
            depthMap[it] = depth
            it.children
        }

        if (children.isNotEmpty())
            children.handleChildren(depth + 1)
    }

    private fun List<Command>.handleParent(parent: Command): Unit = forEach {
        it.parent = parent
        it.children.handleParent(it)
    }

    private fun Config.getConfigCommand(name: String, baseName: String? = null): Command = object : Command(name) {
        init {
            if (baseName == null) children(object : Command("reload") {

            })

            elements.forEach { element ->
                val fullName = if (baseName == null)
                    element.key
                else
                    "$baseName.${element.key}"

                when (element) {
                    is ObjectElement -> {
                        if (element.value == null)
                            children(object : Command(element.key) {
                                override fun CommandContext.execute() {
                                    if (baseName == null)
                                        fail("The value of ${element.key} is expected to be JsonObject, but is currently set to null.")
                                    else
                                        fail("The value of \"$baseName.${element.key}\" is expected to be JsonObject, but is currently set to null.")
                                }
                            })
                        else
                            children(
                                element.value!!.getConfigCommand(
                                    element.key,
                                    fullName
                                )
                            )
                    }

                    is ArrayElement<*> -> {
                        children(object : Command(element.key) {
                            init {
                                usage {
                                    literalArgument("get")
                                    executes {
                                        message {
                                            bold("[!] ", Color.BLUE)
                                            append("\"$fullName\"", Color(180, 85, 227))
                                            append(": ", Color(147, 52, 25))
                                            if (element.value == null) {
                                                append("null", Color(222, 72, 26))
                                            } else {
                                                append("[")
                                                element.value!!.forEachIndexed { index, value ->
                                                    append(value.getHighlightedJsonPrimitive())

                                                    if (index < element.value!!.size - 1)
                                                        append(", ", Color(147, 52, 25))
                                                }
                                                append("]")
                                            }
                                        }
                                    }
                                }

                                usage {
                                    literalArgument("add")
                                    stringArgument("value(s)", StringArgument.Type.PHRASE)

                                    executes {
                                        val values = (typedArgs[1] as String).split(",").map { it.trim() }

                                        when (element) {
                                            is IntegerArrayElement -> {
                                                if (values.any { it.toIntOrNull() == null }) {
                                                    message {
                                                        bold("[!] ", Color.RED)
                                                        append("[")
                                                        values.filter { it.toIntOrNull() == null }
                                                            .forEachIndexed { index, value ->
                                                                append(value, Color(83, 184, 59))
                                                                if (index < values.size - 1)
                                                                    append(", ", Color(147, 52, 25))
                                                            }
                                                        append("]")
                                                        append(" cannot be casted to a integer.")
                                                    }
                                                    return@executes
                                                }

                                                if (element.value == null) {
                                                    element.value = mutableListOf()
                                                }

                                                element.value!!.addAll(values.map { it.toInt() })

                                                message {
                                                    bold("[+] ", Color.GREEN)
                                                    append("added ${values.size} integer ")
                                                    append("[")
                                                    values.forEachIndexed { index, value ->
                                                        append(value, Color(83, 184, 59))
                                                        if (index < values.size - 1)
                                                            append(", ", Color(147, 52, 25))
                                                    }
                                                    append("]")
                                                    append(" to ", Color.GREEN)
                                                    bold("\"$fullName\"", Color(180, 85, 227))
                                                }

                                                saveConfig()
                                            }

                                            is LongArrayElement -> {
                                                if (values.any { it.toLongOrNull() == null }) {
                                                    message {
                                                        bold("[!] ", Color.RED)
                                                        append("[")
                                                        values.filter { it.toLongOrNull() == null }
                                                            .forEachIndexed { index, value ->
                                                                append(value, Color(83, 184, 59))
                                                                if (index < values.size - 1)
                                                                    append(", ", Color(147, 52, 25))
                                                            }
                                                        append("]")
                                                        append(" cannot be casted to a long.")
                                                    }
                                                    return@executes
                                                }

                                                if (element.value == null) {
                                                    element.value = mutableListOf()
                                                }

                                                element.value!!.addAll(values.map { it.toLong() })

                                                message {
                                                    bold("[+] ", Color.GREEN)
                                                    append("added ${values.size} long ")
                                                    append("[")
                                                    values.forEachIndexed { index, value ->
                                                        append(value, Color(83, 184, 59))
                                                        if (index < values.size - 1)
                                                            append(", ", Color(147, 52, 25))
                                                    }
                                                    append("]")
                                                    append(" to ", Color.GREEN)
                                                    bold("\"$fullName\"", Color(180, 85, 227))
                                                }

                                                saveConfig()
                                            }

                                            is FloatArrayElement -> {
                                                if (values.any { it.toFloatOrNull() == null }) {
                                                    message {
                                                        bold("[!] ", Color.RED)
                                                        append("[")
                                                        values.filter { it.toFloatOrNull() == null }
                                                            .forEachIndexed { index, value ->
                                                                append(value, Color(83, 184, 59))
                                                                if (index < values.size - 1)
                                                                    append(", ", Color(147, 52, 25))
                                                            }
                                                        append("]")
                                                        append(" cannot be casted to a float.")
                                                    }
                                                    return@executes
                                                }

                                                if (element.value == null) {
                                                    element.value = mutableListOf()
                                                }

                                                element.value!!.addAll(values.map { it.toFloat() })

                                                message {
                                                    bold("[+] ", Color.GREEN)
                                                    append("added ${values.size} float ")
                                                    append("[")
                                                    values.forEachIndexed { index, value ->
                                                        append(value, Color(83, 184, 59))
                                                        if (index < values.size - 1)
                                                            append(", ", Color(147, 52, 25))
                                                    }
                                                    append("]")
                                                    append(" to ", Color.GREEN)
                                                    bold("\"$fullName\"", Color(180, 85, 227))
                                                }

                                                saveConfig()
                                            }

                                            is DoubleArrayElement -> {
                                                if (values.any { it.toDoubleOrNull() == null }) {
                                                    message {
                                                        bold("[!] ", Color.RED)
                                                        append("[")
                                                        values.filter { it.toDoubleOrNull() == null }
                                                            .forEachIndexed { index, value ->
                                                                append(value, Color(83, 184, 59))
                                                                if (index < values.size - 1)
                                                                    append(", ", Color(147, 52, 25))
                                                            }
                                                        append("]")
                                                        append(" cannot be casted to a double.")
                                                    }
                                                    return@executes
                                                }

                                                if (element.value == null) {
                                                    element.value = mutableListOf()
                                                }

                                                element.value!!.addAll(values.map { it.toDouble() })

                                                message {
                                                    bold("[+] ", Color.GREEN)
                                                    append("added ${values.size} double ")
                                                    append("[")
                                                    values.forEachIndexed { index, value ->
                                                        append(value, Color(83, 184, 59))
                                                        if (index < values.size - 1)
                                                            append(", ", Color(147, 52, 25))
                                                    }
                                                    append("]")
                                                    append(" to ", Color.GREEN)
                                                    bold("\"$fullName\"", Color(180, 85, 227))
                                                }

                                                saveConfig()
                                            }

                                            is StringArrayElement -> {
                                                if (element.value == null) {
                                                    element.value = mutableListOf()
                                                }

                                                element.value!!.addAll(values)

                                                message {
                                                    bold("[+] ", Color.GREEN)
                                                    append("added ${values.size} string ")
                                                    append("[")
                                                    values.forEachIndexed { index, value ->
                                                        append(value, Color(83, 184, 59))
                                                        if (index < values.size - 1)
                                                            append(", ", Color(147, 52, 25))
                                                    }
                                                    append("]")
                                                    append(" to ", Color.GREEN)
                                                    bold("\"$fullName\"", Color(180, 85, 227))
                                                }

                                                saveConfig()
                                            }

                                            is BooleanArrayElement -> {
                                                if (values.any { it.toBooleanStrictOrNull() == null }) {
                                                    message {
                                                        bold("[!] ", Color.RED)
                                                        append("[")
                                                        values.filter { it.toBooleanStrictOrNull() == null }
                                                            .forEachIndexed { index, value ->
                                                                append(value, Color(83, 184, 59))
                                                                if (index < values.size - 1)
                                                                    append(", ", Color(147, 52, 25))
                                                            }
                                                        append("]")
                                                        append(" cannot be casted to a boolean.")
                                                    }
                                                    return@executes
                                                }

                                                if (element.value == null) {
                                                    element.value = mutableListOf()
                                                }

                                                element.value!!.addAll(values.map { it.toBoolean() })

                                                message {
                                                    bold("[+] ", Color.GREEN)
                                                    append("added ${values.size} boolean ")
                                                    append("[")
                                                    values.forEachIndexed { index, value ->
                                                        append(value, Color(83, 184, 59))
                                                        if (index < values.size - 1)
                                                            append(", ", Color(147, 52, 25))
                                                    }
                                                    append("]")
                                                    append(" to ", Color.GREEN)
                                                    bold("\"$fullName\"", Color(180, 85, 227))
                                                }

                                                saveConfig()
                                            }
                                        }
                                    }
                                }

                                usage {
                                    literalArgument("remove")
                                    stringArgument("value(s)", StringArgument.Type.PHRASE)

                                    executes {
                                        if (element.value == null || element.value?.isEmpty() == true) {
                                            message {
                                                bold("[!]", Color.RED)
                                                bold(" \"$fullName\"", Color(180, 85, 227))
                                                append(" has no values.")
                                            }
                                            return@executes
                                        }

                                        val values = (typedArgs[1] as String).split(",").map { it.trim() }
                                        val deleteTarget = element.value!!.filter { values.contains(it.toString()) }
                                            .map { it.toString() }

                                        when (element) {
                                            is IntegerArrayElement -> {
                                                element.value!!.removeIf { values.contains(it.toString()) }

                                                message {
                                                    bold("[-] ", Color.GREEN)
                                                    append("removed ${deleteTarget.size} integer ")
                                                    append("[")
                                                    deleteTarget.forEachIndexed { index, value ->
                                                        append(value, Color(83, 184, 59))
                                                        if (index < deleteTarget.size - 1)
                                                            append(", ", Color(147, 52, 25))
                                                    }
                                                    append("]")
                                                    append(" from ", Color.GREEN)
                                                    bold("\"$fullName\"", Color(180, 85, 227))
                                                }
                                                saveConfig()
                                            }

                                            is LongArrayElement -> {
                                                element.value!!.removeIf { values.contains(it.toString()) }

                                                message {
                                                    bold("[-] ", Color.GREEN)
                                                    append("removed ${deleteTarget.size} long ")
                                                    append("[")
                                                    deleteTarget.forEachIndexed { index, value ->
                                                        append(value, Color(83, 184, 59))
                                                        if (index < deleteTarget.size - 1)
                                                            append(", ", Color(147, 52, 25))
                                                    }
                                                    append("]")
                                                    append(" from ", Color.GREEN)
                                                    bold("\"$fullName\"", Color(180, 85, 227))
                                                }
                                                saveConfig()
                                            }

                                            is FloatArrayElement -> {
                                                element.value!!.removeIf { values.contains(it.toString()) }

                                                message {
                                                    bold("[-] ", Color.GREEN)
                                                    append("removed ${deleteTarget.size} float ")
                                                    append("[")
                                                    deleteTarget.forEachIndexed { index, value ->
                                                        append(value, Color(83, 184, 59))
                                                        if (index < deleteTarget.size - 1)
                                                            append(", ", Color(147, 52, 25))
                                                    }
                                                    append("]")
                                                    append(" from ", Color.GREEN)
                                                    bold("\"$fullName\"", Color(180, 85, 227))
                                                }
                                            }

                                            is DoubleArrayElement -> {
                                                element.value!!.removeIf { values.contains(it.toString()) }

                                                message {
                                                    bold("[-] ", Color.GREEN)
                                                    append("removed ${deleteTarget.size} double ")
                                                    append("[")
                                                    deleteTarget.forEachIndexed { index, value ->
                                                        append(value, Color(83, 184, 59))
                                                        if (index < deleteTarget.size - 1)
                                                            append(", ", Color(147, 52, 25))
                                                    }
                                                    append("]")
                                                    append(" from ", Color.GREEN)
                                                    bold("\"$fullName\"", Color(180, 85, 227))
                                                }
                                                saveConfig()
                                            }

                                            is StringArrayElement -> {
                                                element.value!!.removeIf { values.contains(it) }

                                                message {
                                                    bold("[-] ", Color.GREEN)
                                                    append("removed ${deleteTarget.size} string ")
                                                    append("[")
                                                    deleteTarget.forEachIndexed { index, value ->
                                                        append(value, Color(83, 184, 59))
                                                        if (index < deleteTarget.size - 1)
                                                            append(", ", Color(147, 52, 25))
                                                    }
                                                    append("]")
                                                    append(" from ", Color.GREEN)
                                                    bold("\"$fullName\"", Color(180, 85, 227))
                                                }
                                                saveConfig()
                                            }

                                            is BooleanArrayElement -> {
                                                element.value!!.removeIf { values.contains(it.toString()) }

                                                message {
                                                    bold("[-] ", Color.GREEN)
                                                    append("removed ${deleteTarget.size} boolean ")
                                                    append("[")
                                                    deleteTarget.forEachIndexed { index, value ->
                                                        append(value, Color(83, 184, 59))
                                                        if (index < deleteTarget.size - 1)
                                                            append(", ", Color(147, 52, 25))
                                                    }
                                                    append("]")
                                                    append(" from ", Color.GREEN)
                                                    bold("\"$fullName\"", Color(180, 85, 227))
                                                }
                                                saveConfig()
                                            }

                                            else -> {
                                                return@executes
                                            }
                                        }
                                    }
                                }

                                usage {
                                    literalArgument("clear")

                                    executes {
                                        if (element.value == null || element.value?.isEmpty() == true) {
                                            message {
                                                bold("[!]", Color.RED)
                                                bold(" \"$fullName\"", Color(180, 85, 227))
                                                append(" has no values.")
                                            }
                                            return@executes
                                        }

                                        message {
                                            bold("[-]", Color.GREEN)
                                            append("cleared ${element.value!!.size} items ")
                                            append("[")
                                            element.value!!.forEachIndexed { index, value ->
                                                append(value.toString(), Color(83, 184, 59))
                                                if (index < element.value!!.size - 1)
                                                    append(", ", Color(147, 52, 25))
                                            }
                                            append("]")
                                            append(" from ", Color.GREEN)
                                            bold("\"$fullName\"", Color(180, 85, 227))
                                        }
                                        element.value!!.clear()
                                        saveConfig()
                                    }
                                }
                            }
                        })
                    }
                    else -> {
                        children(object : Command(element.key) {
                            init {
                                usage {
                                    literalArgument("get")
                                    executes {
                                        message {
                                            append("[!] ", Color.BLUE)
                                            append("\"$fullName\"", Color(180, 85, 227))
                                            append(": ", Color(147, 52, 25))
                                            append(element.value.getHighlightedJsonPrimitive())
                                        }
                                    }
                                }

                                usage {
                                    literalArgument("set")

                                    when (element) {
                                        is IntegerElement -> integerArgument("value") {
                                            element.value = typedArgs[1]!! as Int

                                            message {
                                                bold("[+] ", Color.GREEN)
                                                append("set ", Color.GREEN)
                                                append(element.value.toString())
                                                append(" to ", Color.GREEN)
                                                bold("\"$fullName\"", Color(180, 85, 227))
                                            }

                                            saveConfig()
                                        }
                                        is LongElement -> longArgument("value") {
                                            element.value = typedArgs[1]!! as Long

                                            message {
                                                bold("[+] ", Color.GREEN)
                                                append("set ", Color.GREEN)
                                                append(element.value.toString())
                                                append(" to ", Color.GREEN)
                                                bold("\"$fullName\"", Color(180, 85, 227))
                                            }

                                            saveConfig()
                                        }
                                        is FloatElement -> floatArgument("value") {
                                            element.value = typedArgs[1]!! as Float

                                            message {
                                                bold("[+] ", Color.GREEN)
                                                append("set ", Color.GREEN)
                                                append(element.value.toString())
                                                append(" to ", Color.GREEN)
                                                bold("\"$fullName\"", Color(180, 85, 227))
                                            }

                                            saveConfig()
                                        }
                                        is DoubleElement -> doubleArgument("value") {
                                            element.value = typedArgs[1]!! as Double

                                            message {
                                                bold("[+] ", Color.GREEN)
                                                append("set ", Color.GREEN)
                                                append(element.value.toString())
                                                append(" to ", Color.GREEN)
                                                bold("\"$fullName\"", Color(180, 85, 227))
                                            }

                                            saveConfig()
                                        }
                                        is StringElement -> stringArgument("value") {
                                            element.value = typedArgs[1]!! as String

                                            message {
                                                bold("[+] ", Color.GREEN)
                                                append("set ", Color.GREEN)
                                                append(element.value.toString())
                                                append(" to ", Color.GREEN)
                                                bold("\"$fullName\"", Color(180, 85, 227))
                                            }

                                            saveConfig()
                                        }
                                        is BooleanElement -> booleanArgument("value") {
                                            element.value = typedArgs[1]!! as Boolean

                                            message {
                                                bold("[+] ", Color.GREEN)
                                                append("set ", Color.GREEN)
                                                append(element.value.toString())
                                                append(" to ", Color.GREEN)
                                                bold("\"$fullName\"", Color(180, 85, 227))
                                            }

                                            saveConfig()
                                        }
                                    }
                                }
                            }
                        })
                    }
                }
            }

            usage {
                literalArgument("get")
                executes {
                    message {
                        bold("[!] ", Color.BLUE)
                        append(if (baseName == null) "root" else "\"$baseName\"", Color(180, 85, 227))
                        appendln(": ", Color(147, 52, 25))
                        appendln("{")
                        getHighlightedJsonObject(1, elements)
                        appendln("}")
                        appendln()
                    }
                }
            }
        }
    }

    private fun saveConfig() {
        val json = config!!.getJsonString(gson)

        Path("./plugins/config/${flyLib.plugin.name}.json")
            .also { it.parent.createDirectories() }
            .also { if (!it.exists()) it.createFile() }
            .writeText(json)
    }

    private fun loadConfig() {
        if (!Path("./plugins/config/${flyLib.plugin.name}.json").exists()) return

        val json = Path("./plugins/config/${flyLib.plugin.name}.json").readText().asJsonObject()

        config!!.loadJsonObject(json)
    }

    private fun Config.loadJsonObject(json: JsonObject) {
        elements.forEach { element ->
            when (element) {
                is IntegerElement -> element.value = json.getIntOrNull(element.key)
                is LongElement -> element.value = json.getLongOrNull(element.key)
                is FloatElement -> element.value = json.getFloatOrNull(element.key)
                is DoubleElement -> element.value = json.getDoubleOrNull(element.key)
                is StringElement -> element.value = json.getStringOrNull(element.key)
                is BooleanElement -> element.value = json.getBooleanOrNull(element.key)
                is IntegerArrayElement -> element.value =
                    json.getIntArrayOrNull(element.key)?.filterNotNull()?.toMutableList()
                is LongArrayElement -> element.value =
                    json.getLongArrayOrNull(element.key)?.filterNotNull()?.toMutableList()
                is FloatArrayElement -> element.value =
                    json.getFloatArrayOrNull(element.key)?.filterNotNull()?.toMutableList()
                is DoubleArrayElement -> element.value =
                    json.getDoubleArrayOrNull(element.key)?.filterNotNull()?.toMutableList()
                is StringArrayElement -> element.value =
                    json.getStringArrayOrNull(element.key)?.filterNotNull()?.toMutableList()
                is BooleanArrayElement -> element.value =
                    json.getBooleanArrayOrNull(element.key)?.filterNotNull()?.toMutableList()
                is ObjectElement -> json.getObjectOrNull(element.key)?.let { element.value!!.loadJsonObject(it) }
            }
        }
    }

    private fun ComponentBuilder.getHighlightedJsonObject(depth: Int, elements: List<ConfigElement<*>>) {
        val tabSize = "  ".repeat(depth)
        elements.forEachIndexed { index, element ->
            when (element) {
                is ObjectElement -> {
                    append(tabSize)
                    append("\"${element.key}\"", Color(180, 85, 227))
                    append(": ", Color(147, 52, 25))
                    appendln("{")
                    getHighlightedJsonObject(depth + 1, element.value!!.elements)
                    append("$tabSize}")
                    if (index < elements.size - 1)
                        appendln(",", Color(147, 52, 25))
                    else
                        appendln()
                }
                is ArrayElement<*> -> {
                    append(tabSize)
                    append("\"${element.key}\"", Color(180, 85, 227))
                    append(": ", Color(147, 52, 25))
                    appendln("[")
                    element.value!!.forEachIndexed { arrayElementIndex, arrayElement ->
                        append("$tabSize  ")
                        append(arrayElement.getHighlightedJsonPrimitive())
                        if (arrayElementIndex < element.value!!.size - 1)
                            appendln(",", Color(147, 52, 25))
                        else
                            appendln()
                    }
                    append("$tabSize]")
                    if (index < elements.size - 1)
                        appendln(",", Color(147, 52, 25))
                    else
                        appendln()
                }
                else -> {
                    append(tabSize)
                    append("\"${element.key}\"", Color(180, 85, 227))
                    append(": ", Color(147, 52, 25))
                    append(element.value.getHighlightedJsonPrimitive())
                    if (index < elements.size - 1)
                        appendln(",", Color(147, 52, 25))
                    else
                        appendln()
                }
            }
        }
    }

    private fun Any?.getHighlightedJsonPrimitive() = when (this) {
        is String -> "\"${toString()}\"".component(Color(83, 184, 59))
        is Number -> toString().component(Color(58, 165, 239))
        else -> toString().component(Color(255, 118, 76))
    }

    private fun Config.getJsonString(gson: Gson) = gson.toJson(mapJsonObject())!!

    private fun Config.mapJsonObject(): Map<String, Any?> = elements.associate {
        it.key to when (it) {
            is ObjectElement -> it.value?.mapJsonObject()
            else -> it.value
        }
    }
}