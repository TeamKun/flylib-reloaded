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
import dev.kotx.flylib.command.internal.Permission
import dev.kotx.flylib.utils.*
import kotlinx.coroutines.*
import net.minecraft.server.v1_16_R3.*
import org.bukkit.*
import org.bukkit.command.*
import org.bukkit.craftbukkit.v1_16_R3.*
import org.bukkit.craftbukkit.v1_16_R3.command.*
import org.bukkit.entity.*
import org.bukkit.permissions.*
import org.bukkit.plugin.java.*
import org.koin.core.component.*
import org.slf4j.*
import java.lang.invoke.*
import kotlin.collections.set


class CommandHandler(
    private val commands: List<Command>
) : FlyLibComponent {
    private val plugin: JavaPlugin by inject()
    private val logger: Logger by inject()
    private val scope = CoroutineScope(Dispatchers.Default + CoroutineName("FlyLib Command Dispatcher"))

    internal fun onEnable() {
        logger.info("[CommandHandler] Registering commands: (${commands.size})")

        logger.info("[CommandHandler] Adding permissions")
        plugin.server.pluginManager.addPermission(
            org.bukkit.permissions.Permission(
                "flylib.command.op",
                PermissionDefault.OP
            )
        )
        plugin.server.pluginManager.addPermission(
            org.bukkit.permissions.Permission(
                "flylib.command.notop",
                PermissionDefault.NOT_OP
            )
        )
        plugin.server.pluginManager.addPermission(
            org.bukkit.permissions.Permission(
                "flylib.command.everyone",
                PermissionDefault.TRUE
            )
        )

        commands.forEach { command ->
            logger.info("[CommandHandler] Registering command: ${command.name}")

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
        }
    }

    internal fun onLoad() {
        commands.forEach { cmd ->
            logger.info("[CommandHandler] Registering permissions: ${cmd.name}")

            plugin.server.commandMap.getCommand(cmd.name)?.permission = when (cmd.permission) {
                Permission.OP -> "flylib.command.op"
                Permission.NOT_OP -> "flylib.command.notop"
                Permission.EVERYONE -> "flylib.command.everyone"
            }

            plugin.server.commandMap.getCommand("minecraft:${cmd.name}")?.permission = when (cmd.permission) {
                Permission.OP -> "flylib.command.op"
                Permission.NOT_OP -> "flylib.command.notop"
                Permission.EVERYONE -> "flylib.command.everyone"
            }

            cmd.aliases.forEach {
                plugin.server.commandMap.getCommand(it)?.permission = when (cmd.permission) {
                    Permission.OP -> "flylib.command.op"
                    Permission.NOT_OP -> "flylib.command.notop"
                    Permission.EVERYONE -> "flylib.command.everyone"
                }
                plugin.server.commandMap.getCommand("minecraft:$it")?.permission = when (cmd.permission) {
                    Permission.OP -> "flylib.command.op"
                    Permission.NOT_OP -> "flylib.command.notop"
                    Permission.EVERYONE -> "flylib.command.everyone"
                }
            }
        }
    }

    internal fun onDisable() {
        val root = ((Bukkit.getServer() as CraftServer).server as MinecraftServer).commandDispatcher.a().root
        val commandNodes = MethodHandles.privateLookupIn(SimpleCommandMap::class.java, MethodHandles.lookup())
            .findVarHandle(SimpleCommandMap::class.java, "knownCommands", MutableMap::class.java)
            .get(Bukkit.getCommandMap()) as MutableMap<String, org.bukkit.command.Command>

        logger.info("Unregistering commands: (${commands.size})")
        commands.forEach {
            logger.info("Unregistering command: ${it.name}")

            root.removeCommand(it.name)
            root.removeCommand("minecraft:${it.name}")
            commandNodes.remove(it.name)
            commandNodes.remove("minecraft:${it.name}")

            it.aliases.forEach {
                root.removeCommand(it)
                root.removeCommand("minecraft:${it}")
                commandNodes.remove(it)
                commandNodes.remove("minecraft:${it}")
            }
        }

        logger.info("Removing permissions")
        plugin.server.pluginManager.removePermission("flylib.command.op")
        plugin.server.pluginManager.removePermission("flylib.command.notop")
        plugin.server.pluginManager.removePermission("flylib.command.everyone")
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
                        if (child.runAsync) scope.launch { child.apply { it.asFlyLibContext(child, emptyList(), depthMap[child] ?: 0).execute() } }
                        else child.apply { it.asFlyLibContext(child, emptyList(), depthMap[child] ?: 0).execute() }
                        1
                    }
                child.handle(childBase)
                base.then(childBase)

                child.aliases.forEach { it ->
                    val childAliasBase = LiteralArgumentBuilder
                        .literal<CommandListenerWrapper?>(it)
                        .requires { child.validate(it.bukkitSender) }
                        .executes {
                            if (child.runAsync)
                                scope.launch { child.apply { it.asFlyLibContext(child, emptyList(), depthMap[child] ?: 0).execute() } }
                            else
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
                if (command.runAsync)
                    scope.launch { command.apply { it.asFlyLibContext(command, emptyList(), 0).execute() } }
                else
                    command.apply { it.asFlyLibContext(command, emptyList(), 0).execute() }
                1
            }

        command.handle(base)

        val dispatcher = ((Bukkit.getServer() as CraftServer).server as MinecraftServer).commandDispatcher
        dispatcher.a().register(base)

        val commandNodes = MethodHandles.privateLookupIn(SimpleCommandMap::class.java, MethodHandles.lookup())
            .findVarHandle(SimpleCommandMap::class.java, "knownCommands", MutableMap::class.java)
            .get(Bukkit.getCommandMap()) as MutableMap<String, org.bukkit.command.Command>

        commandNodes[name] = VanillaCommandWrapper(
            dispatcher,
            base.build()
        )
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
            val validPermission = when (usage.permission ?: command.permission) {
                Permission.OP -> it.bukkitSender.isOp
                Permission.NOT_OP -> !it.bukkitSender.isOp
                Permission.EVERYONE -> true
            }

            if (!validPermission) return@requires false

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

            if (usage.runAsync ?: command.runAsync)
                scope.launch { usage.action?.apply { ctx.execute() } ?: command.apply { ctx.execute() } }
            else
                usage.action?.apply { ctx.execute() } ?: command.apply { ctx.execute() }

            1
        }
    }

    class Builder {
        private val commands = mutableListOf<Command>()

        /**
         * Changes the default command settings. See CommandDefault below for details.
         * @param action The lambda expression that makes up the Command Default.
         *
         * @see CommandDefault
         */
        fun defaultConfiguration(action: CommandDefault.Action): Builder {
            CommandDefault.apply { action.apply { initialize() } }
            return this
        }

        /**
         * Registers the specified command. It is not necessary to register commands or permissions in plugin.yml.
         * @param commands The commands you want to register.
         */
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

        /**
         * Registers the specified command. It is not necessary to register commands or permissions in plugin.yml.
         * @param name The name of the command you want to register.
         * @param action Builder lambda expressions that make up the Command.
         */
        fun register(name: String, action: Command.Builder.Action): Builder {
            register(Command.Builder(name).apply { action.apply { initialize() } }.build())
            return this
        }

        /**
         * Build CommandHandler
         *
         * @return CommandHandler Instance
         */
        fun build() = CommandHandler(commands)

        fun interface Action {
            fun Builder.initialize()
        }
    }
}