package dev.kotx.flylib.command.parameters

import dev.kotx.flylib.command.ConfigParameter

class StringParameter(override val key: String, override val value: String?) : ConfigParameter<String>