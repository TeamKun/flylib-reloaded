/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command.elements

import dev.kotx.flylib.command.ConfigElement

class BooleanElement(override val key: String, override var value: Boolean?) : ConfigElement<Boolean>