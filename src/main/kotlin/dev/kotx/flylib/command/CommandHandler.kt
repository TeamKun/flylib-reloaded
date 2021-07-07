/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */
package dev.kotx.flylib.command

import com.mojang.brigadier.*
import com.mojang.brigadier.builder.*
import com.mojang.brigadier.context.CommandContext
import dev.kotx.flylib.*
import dev.kotx.flylib.command.internal.*
import dev.kotx.flylib.utils.*
import net.minecraft.server.v1_16_R3.*
import org.bukkit.*
import org.bukkit.command.*
import org.bukkit.craftbukkit.v1_16_R3.*
import org.bukkit.craftbukkit.v1_16_R3.command.*
import org.bukkit.entity.*
import org.bukkit.plugin.java.*
import org.koin.core.component.*
import java.lang.invoke.*
import kotlin.collections.set

typealias Perm = org.bukkit.permissions.Permission

class CommandHandler(
    private val commands: List<Command>
) : FlyLibComponent {
    private val plugin: JavaPlugin by inject()

    internal fun onEnable() {
        commands.forEach { command ->
            register(
                command,
                command.name
            )
            command.aliases.forEach {
                register(
                    command,
                    it
                )
            }

            plugin.server.pluginManager.addPermission(Perm(getPermissionNameForCommand(plugin, command), command.permission.default))

            command.usages.filter { it.permission != null }.forEach {
                plugin.server.pluginManager.addPermission(Perm(getPermissionNameForUsage(plugin, command, it), it.permission!!.default))
            }
        }
    }

    internal fun onLoad() {
        commands.forEach { command ->
            plugin.server.commandMap.getCommand(command.name)?.permission = getPermissionNameForCommand(plugin, command)
            plugin.server.commandMap.getCommand("minecraft:${command.name}")?.permission = getPermissionNameForCommand(plugin, command)
            command.aliases.forEach {
                plugin.server.commandMap.getCommand(it)?.permission = getPermissionNameForCommand(plugin, command)
                plugin.server.commandMap.getCommand("minecraft:$it")?.permission = getPermissionNameForCommand(plugin, command)
            }
        }
    }

    internal fun onDisable() {
        val root = ((Bukkit.getServer() as CraftServer).server as MinecraftServer).commandDispatcher.a().root
        val commandNodes = MethodHandles.privateLookupIn(SimpleCommandMap::class.java, MethodHandles.lookup())
            .findVarHandle(SimpleCommandMap::class.java, "knownCommands", MutableMap::class.java)
            .get(Bukkit.getCommandMap()) as MutableMap<String, org.bukkit.command.Command>

        commands.forEach { command ->
            root.removeCommand(command.name)
            root.removeCommand("minecraft:${command.name}")
            commandNodes.remove(command.name)
            commandNodes.remove("minecraft:${command.name}")
            command.aliases.forEach {
                root.removeCommand(it)
                root.removeCommand("minecraft:${it}")
                commandNodes.remove(it)
                commandNodes.remove("minecraft:${it}")
            }

            plugin.server.pluginManager.removePermission(getPermissionNameForCommand(plugin, command))
            command.usages.filter { it.permission != null }.forEach {
                plugin.server.pluginManager.removePermission(getPermissionNameForUsage(plugin, command, it))
            }
        }
    }

    private fun register(
        command: Command,
        name: String
    ) {
        val depthMap = mutableMapOf<Command, Int>()
        var depth = 0
        fun List<Command>.handleDepthMap() {
            if (!isEmpty()) {
                depth++
                forEach { depthMap[it] = depth }
                flatMap { it.children }.handleDepthMap()
            }
        }
        command.children.handleDepthMap()

        fun Command.handle(base: LiteralArgumentBuilder<CommandListenerWrapper>) {
            usages.forEach { usage ->
                var usageBase: ArgumentBuilder<CommandListenerWrapper, *>? = null
                usage.args.reversed().forEach {
                    val ch = usageBase
                    usageBase = getArgumentBuilder(it)
                    if (ch != null)
                        usageBase?.then(ch)
                    usageBase?.setupArgument(usage, it, this, depthMap[this] ?: 0)
                }
                base.then(usageBase)
            }

            children.forEach { child ->
                val childBase = LiteralArgumentBuilder
                    .literal<CommandListenerWrapper?>(child.name)
                    .requires { child.validate(it.bukkitSender) }
                    .executes {
                        child.apply { it.asFlyLibContext(child, emptyList(), depthMap[child] ?: 0).execute() }
                        1
                    }

                child.handle(childBase)
                base.then(childBase)
                child.aliases.forEach { it ->
                    val childAliasBase = LiteralArgumentBuilder
                        .literal<CommandListenerWrapper?>(it)
                        .requires { child.validate(it.bukkitSender) }
                        .executes {
                            child.apply { it.asFlyLibContext(child, emptyList(), depthMap[child] ?: 0).execute() }
                            1
                        }
                    child.handle(childAliasBase)
                    base.then(childAliasBase)
                }
            }
        }

        val base = LiteralArgumentBuilder
            .literal<CommandListenerWrapper?>(name)
            .requires { command.validate(it.bukkitSender) }
            .executes {
                command.apply { it.asFlyLibContext(command, emptyList(), 0).execute() }
                1
            }

        command.handle(base)

        val dispatcher = ((Bukkit.getServer() as CraftServer).server as MinecraftServer).commandDispatcher
        dispatcher.a().register(base)

        val commandNodes = MethodHandles.privateLookupIn(SimpleCommandMap::class.java, MethodHandles.lookup())
            .findVarHandle(SimpleCommandMap::class.java, "knownCommands", MutableMap::class.java)
            .get(Bukkit.getCommandMap()) as MutableMap<String, org.bukkit.command.Command>

        commandNodes[name] = VanillaCommandWrapper(dispatcher, base.build())
    }

    private fun getArgumentBuilder(argument: Argument<*>): ArgumentBuilder<CommandListenerWrapper, *> = if (argument.type == null)
        LiteralArgumentBuilder.literal(argument.name)
    else
        RequiredArgumentBuilder.argument(argument.name, argument.type)

    private fun ArgumentBuilder<CommandListenerWrapper, *>.setupArgument(
        usage: Usage,
        argument: Argument<*>,
        command: Command,
        depth: Int
    ) {
        requires {
            if (!it.bukkitSender.hasPermission(getPermissionNameForUsage(plugin, command, usage))) return@requires false
            val playerOnly = usage.playerOnly ?: command.playerOnly
            val validSender = !playerOnly || playerOnly && it.bukkitSender is Player
            if (!validSender) return@requires false
            true
        }
        if (this is RequiredArgumentBuilder<CommandListenerWrapper, *> && argument.tabComplete != null) suggests { ctx, builder ->
            argument.tabComplete.run {
                val context = ctx.asFlyLibContext(command, usage.args.toList(), depth)
                Suggestion.Builder(
                    context.command,
                    context.plugin,
                    context.sender,
                    context.message,
                    context.args,
                    context.typedArgs
                ).run {
                    complete()
                    build()
                }
            }.forEach { it ->
                val toolTipMessage = it.tooltip?.let { LiteralMessage(it) }
                builder.suggest(it.content, toolTipMessage)
            }
            builder.buildFuture()
        }
        executes {
            val ctx = (it as CommandContext<CommandListenerWrapper>).asFlyLibContext(command, usage.args.toList(), depth)
            usage.action?.apply { ctx.execute() } ?: command.apply { ctx.execute() }
            1
        }
    }

    class Builder {
        private val commands = mutableListOf<Command>()

        fun defaultConfiguration(action: CommandDefault.Action): Builder {
            CommandDefault.apply { action.apply { initialize() } }
            return this
        }

        fun register(vararg commands: Command): Builder {
            fun List<Command>.setParent(parent: Command): Unit = forEach {
                it.parent = parent
                it.children.setParent(it)
            }
            commands.forEach {
                it.children.setParent(it)
                this.commands.add(it)
            }
            return this
        }

        fun register(name: String, action: Command.Builder.Action): Builder {
            register(Command.Builder(name).apply { action.apply { initialize() } }.build())
            return this
        }

        fun build() = CommandHandler(commands)

        fun interface Action {
            fun Builder.initialize()
        }
    }
}