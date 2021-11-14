package dev.kotx.flylib.command

import kotlinx.serialization.json.JsonElement

class Config(
    val values: Map<String, JsonElement>
)