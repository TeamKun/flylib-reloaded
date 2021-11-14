package dev.kotx.flylib.command

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

class ConfigBuilder {
    private val values = mutableMapOf<String, JsonElement>()

    fun integer(key: String, value: Int): ConfigBuilder {
        values[key] = JsonPrimitive(value)

        return this
    }
}