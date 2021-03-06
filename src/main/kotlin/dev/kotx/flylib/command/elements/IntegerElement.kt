/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command.elements

import dev.kotx.flylib.command.ConfigElement

class IntegerElement(override val key: String, override var value: Int?, val min: Int, val max: Int) :
    ConfigElement<Int>