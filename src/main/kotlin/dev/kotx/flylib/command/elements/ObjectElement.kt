/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command.elements

import dev.kotx.flylib.command.Config
import dev.kotx.flylib.command.ConfigElement

class ObjectElement(override val key: String, override var value: Config?) : ConfigElement<Config>