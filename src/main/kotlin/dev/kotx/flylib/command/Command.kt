/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command

/**
 * An Command used for FlyLib
 */
abstract class Command<T>(
    internal val name: String,
) {
    /**
     * Command description
     * Used for the default help message.
     */
    @JvmField
    internal var description: String? = null

    /**
     * Privileges required to execute commands.
     * There is no need to register permissions in plugin.yml, they will be automatically registered and removed as plugins are loaded and unloaded.
     */
    @JvmField
    internal var permission: Permission? = null

    /**
     * Another name for the command.
     * You can use this alias to invoke the exact same command.
     */
    @JvmField
    internal val aliases: MutableList<String> = mutableListOf()

    internal val fullCommand: List<Command<T>>
        get() {
            val commands = mutableListOf<Command<T>>()
            var current: Command<T>? = this

            do {
                commands.add(current!!)
                current = current.parent
            } while (current != null)

            return commands.reversed()
        }

    /**
     * Command definition and usage.
     * If you want to add arguments to the command, add them here.
     * The default help message will use this usage for the message.
     *
     * ## Java Example
     * ```java
     * public PrintNumberCommand() {
     *      super("printnumber");
     *      usage(builder -> builder
     *              .description("Print your number.")
     *              .integerArgument("number")
     *              .executes(context -> context.send("Number: " + context.args[0]))
     *      );
     * }
     * ```
     *
     * ## Kotlin Example
     * ```kotlin
     * init {
     *      usage {
     *          description("Print your number.")
     *          integerArgument("number")
     *          executes {
     *              send("Number: ${args.first()}")
     *          }
     *      }
     * }
     * ```
     */
    @JvmField
    internal val usages: MutableList<Usage<T>> = mutableListOf()

    /**
     * Command usage example. Used for the default help message.
     */
    @JvmField
    internal val examples: MutableList<String> = mutableListOf()

    /**
     * A child command. It is automatically registered as a command.
     */
    @JvmField
    internal val children: MutableList<Command<T>> = mutableListOf()

    internal var parent: Command<T>? = null

    /**
     * The block that executes the command. The default is to display help for that command.
     */
    open fun CommandContext<T>.execute() {
        sendHelp()
    }

    /**
     * Set command description
     * Used for the default help message.
     */
    protected fun description(description: String) {
        this.description = description
    }

    /**
     * Set privileges required to execute commands.
     * There is no need to register permissions in plugin.yml, they will be automatically registered and removed as plugins are loaded and unloaded.
     * Also, if no permissions are specified (null), the default permissions that can be specified in Fly Lib's builder will be assigned.
     */
    protected fun permission(permission: Permission) {
        this.permission = permission
    }

    /**
     * Add another name for the command.
     * You can use this alias to invoke the exact same command.
     */
    protected fun alias(vararg alias: String) {
        this.aliases.addAll(alias)
    }

    /**
     * Add command definition and usage.
     * If you want to add arguments to the command, add them here.
     * The default help message will use this usage for the message.
     *
     * ## Java Example
     * ```java
     * public PrintNumberCommand() {
     *      super("printnumber");
     *      usage(builder -> builder
     *              .description("Print your number.")
     *              .integerArgument("number")
     *              .executes(context -> context.send("Number: " + context.args[0]))
     *      );
     * }
     * ```
     *
     * ## Kotlin Example
     * ```kotlin
     * init {
     *      usage {
     *          description("Print your number.")
     *          integerArgument("number")
     *          executes {
     *              send("Number: ${args.first()}")
     *          }
     *      }
     * }
     * ```
     */
    protected fun usage(builder: UsageAction<T>) {
        val usage = UsageBuilder<T>().apply { builder.apply { initialize() } }.build()
        usages.add(usage)
    }

    /**
     * Add command usage example. Used for the default help message.
     */
    protected fun example(vararg example: String) {
        this.examples.addAll(example)
    }

    /**
     * Add child command. It is automatically registered as a command.
     */
    protected fun children(vararg children: Command<T>) {
        this.children.addAll(children)
    }
}