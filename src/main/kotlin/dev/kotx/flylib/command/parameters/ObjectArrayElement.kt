package dev.kotx.flylib.command.parameters

import dev.kotx.flylib.command.Config

class ObjectArrayElement(override val key: String, override val value: Array<Config>?) : ArrayElement<Config>