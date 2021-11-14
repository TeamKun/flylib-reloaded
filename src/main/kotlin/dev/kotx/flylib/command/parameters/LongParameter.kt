package dev.kotx.flylib.command.parameters

import dev.kotx.flylib.command.ConfigParameter

class LongParameter(override val key: String, override val value: Long?, val min: Long, val max: Long) : ConfigParameter<Long>