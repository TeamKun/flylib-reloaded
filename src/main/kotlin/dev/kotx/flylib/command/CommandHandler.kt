/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command

import dev.kotx.flylib.*

abstract class CommandHandler(protected val commands: List<Command>) {
    abstract val flyLib: FlyLib
}