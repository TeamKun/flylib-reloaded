/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command

import com.google.gson.Gson
import dev.kotx.flylib.command.elements.ObjectElement

class Config(
    val elements: List<ConfigElement<*>>,
) {
    fun getJsonString(gson: Gson) = gson.toJson(mapJsonObject())!!

    private fun mapJsonObject(): Map<String, Any?> = elements.associate {
        it.key to when (it) {
            is ObjectElement -> it.value?.mapJsonObject()
            else -> it.value
        }
    }
}