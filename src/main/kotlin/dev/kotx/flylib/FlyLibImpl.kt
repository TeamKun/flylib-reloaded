/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib

import dev.kotx.flylib.command.*
import org.bukkit.plugin.java.*

internal class FlyLibImpl(override val plugin: JavaPlugin) : FlyLib {
    override val commandHandler = CommandHandlerImpl(this)
}