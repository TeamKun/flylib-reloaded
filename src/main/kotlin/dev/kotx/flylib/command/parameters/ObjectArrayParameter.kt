package dev.kotx.flylib.command.parameters

import dev.kotx.flylib.command.Config

class ObjectArrayParameter(override val key: String, override val value: Array<Config>?) : ArrayParameter<Config>