package dev.kotx.flylib.command.parameters

import dev.kotx.flylib.command.ConfigParameter

class DoubleParameter(override val key: String, override val value: Double?, val min: Double, val max: Double) :
    ConfigParameter<Double>