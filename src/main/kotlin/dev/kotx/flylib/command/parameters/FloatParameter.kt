package dev.kotx.flylib.command.parameters

import dev.kotx.flylib.command.ConfigParameter

class FloatParameter(override val key: String, override val value: Float?, val min: Float, val max: Float) : ConfigParameter<Float>