/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command

import dev.kotx.flylib.*

internal class CommandHandlerImpl(override val flyLib: FlyLib, commands: List<Command>) : CommandHandler(commands) {
    internal fun enable() {
        commands.forEach(::register)
    }

    internal fun disable() {

    }

    internal fun load() {

    }

    private fun register(command: Command) {

    }
}