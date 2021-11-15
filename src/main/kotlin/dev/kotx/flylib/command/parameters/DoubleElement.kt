package dev.kotx.flylib.command.parameters

import dev.kotx.flylib.command.ConfigElement

class DoubleElement(override val key: String, override val value: Double?, val min: Double, val max: Double) :
    ConfigElement<Double>