package dev.kotx.flylib.command.parameters

import dev.kotx.flylib.command.ConfigElement

class BooleanElement(override val key: String, override val value: Boolean?) : ConfigElement<Boolean>