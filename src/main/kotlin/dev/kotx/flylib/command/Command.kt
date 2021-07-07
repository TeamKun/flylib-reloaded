package dev.kotx.flylib.command

import dev.kotx.flylib.*
import dev.kotx.flylib.command.internal.*
import dev.kotx.flylib.utils.*
import org.bukkit.command.*
import org.bukkit.entity.*
import org.bukkit.plugin.java.*
import org.koin.core.component.*

abstract class Command(
    val name: String
) : FlyLibComponent {
    internal val plugin by inject<JavaPlugin>()
    val flyLib: FlyLib by inject()
    open var description: String = CommandDefault.getDescription()

    open val aliases = mutableListOf<String>()

    open val usages = mutableListOf<Usage>()

    open val examples = mutableListOf<String>()

    open var permission: Permission = CommandDefault.getPermission()

    open var playerOnly: Boolean = CommandDefault.isPlayerOnly()

    open val children = mutableListOf<Command>()

    var parent: Command? = null
    fun validate(sender: CommandSender): Boolean {
        if (!sender.hasPermission("${plugin.name.lowercase()}.command.${fullCommand.joinToString(".") { it.name }.lowercase()}")) return false
        val validSender = !playerOnly || playerOnly && sender is Player
        if (!validSender) return false
        return true
    }

    open fun CommandContext.execute() {
        sendHelp()
    }

    protected fun CommandContext.sendHelp() {
        CommandDefault.getHelp().apply { execute() }
    }

    fun usage(action: Usage.Builder.Action) {
        usages.add(Usage.Builder().apply { action.apply { initialize() } }.build())
    }

    fun example(vararg example: String) {
        examples.addAll(example)
    }

    fun alias(vararg alias: String) {
        aliases.addAll(alias)
    }

    fun child(vararg child: Command) {
        children.addAll(child)
    }

    class Builder(
        private val name: String
    ) {
        private var description: String? = null
        private var permission: Permission? = null
        private var playerOnly: Boolean? = null
        private var action: CommandContext.Action? = null
        private val aliases = mutableListOf<String>()
        private val usages = mutableListOf<Usage>()
        private val examples = mutableListOf<String>()
        private val children = mutableListOf<Command>()
        fun description(description: String): Builder {
            this.description = description
            return this
        }

        fun permission(permission: Permission): Builder {
            this.permission = permission
            return this
        }

        fun playerOnly(playerOnly: Boolean): Builder {
            this.playerOnly = playerOnly
            return this
        }

        fun alias(vararg alias: String): Builder {
            aliases.addAll(alias)
            return this
        }

        fun usage(usage: Usage): Builder {
            this.usages.add(usage)
            return this
        }

        fun usage(action: Usage.Builder.Action): Builder {
            this.usages.add(Usage.Builder().apply { action.apply { initialize() } }.build())
            return this
        }

        fun example(vararg example: String): Builder {
            this.examples.addAll(example)
            return this
        }

        fun child(vararg child: Command): Builder {
            this.children.addAll(child)
            return this
        }

        fun executes(action: CommandContext.Action): Builder {
            this.action = action
            return this
        }

        fun build() = object : Command(name) {
            init {
                this@Builder.description?.also { this.description = it }
                this@Builder.permission?.also { this.permission = it }
                this@Builder.playerOnly?.also { this.playerOnly = it }
                aliases.addAll(this@Builder.aliases)
                usages.addAll(this@Builder.usages)
                examples.addAll(this@Builder.examples)
                children.addAll(this@Builder.children)
            }

            override fun CommandContext.execute() {
                this@Builder.action?.apply { execute() } ?: sendHelp()
            }
        }

        fun interface Action {
            fun Builder.initialize()
        }
    }
}