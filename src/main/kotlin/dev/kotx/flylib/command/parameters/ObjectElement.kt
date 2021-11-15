package dev.kotx.flylib.command.parameters

import dev.kotx.flylib.command.Config
import dev.kotx.flylib.command.ConfigElement

class ObjectElement(override val key: String, override val value: Config?) : ConfigElement<Config>