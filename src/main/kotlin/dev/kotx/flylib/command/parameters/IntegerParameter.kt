package dev.kotx.flylib.command.parameters

import dev.kotx.flylib.command.ConfigParameter

class IntegerParameter(override val key: String, override val value: Int?, val min: Int, max: Int) : ConfigParameter<Int>