package dev.kotx.flylib.command.parameters

import dev.kotx.flylib.command.Config
import dev.kotx.flylib.command.ConfigParameter

class ObjectParameter(override val key: String, override val value: Config?) : ConfigParameter<Config>