package dev.kotx.flylib.command.parameters

import dev.kotx.flylib.command.ConfigParameter

class BooleanParameter(override val key: String, override val value: Boolean?) : ConfigParameter<Boolean>