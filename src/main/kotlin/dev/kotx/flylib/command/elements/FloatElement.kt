/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command.elements

import dev.kotx.flylib.command.ConfigElement

class FloatElement(override val key: String, override var value: Float?, val min: Float, val max: Float) :
    ConfigElement<Float>