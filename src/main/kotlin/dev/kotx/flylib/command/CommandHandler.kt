/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command

import dev.kotx.flylib.FlyLib

/**
 * An interface for CommandHandlerImpl to hiding inner method
 */
interface CommandHandler {
    val flyLib: FlyLib
}