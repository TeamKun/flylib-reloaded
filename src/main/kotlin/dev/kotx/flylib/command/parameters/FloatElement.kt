/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command.parameters

import dev.kotx.flylib.command.ConfigElement

class FloatElement(override val key: String, override val value: Float?, val min: Float, val max: Float) :
    ConfigElement<Float>