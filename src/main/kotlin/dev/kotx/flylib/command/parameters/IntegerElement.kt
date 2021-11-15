/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command.parameters

import dev.kotx.flylib.command.ConfigElement

class IntegerElement(override val key: String, override val value: Int?, val min: Int, max: Int) : ConfigElement<Int>