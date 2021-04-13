/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command.complete.entity

enum class EntitySelectorType(
    val isValid: (String) -> Boolean
) {
    NUMBER(
        {
            it.toDoubleOrNull() != null
        }
    ),

    RANGE(
        {
            it.matches("[0-9]+".toRegex())
                    || it.matches("[0-9]+\\.\\.[0-9]+".toRegex())
                    || it.matches("\\.\\.[0-9]+".toRegex())
                    || it.matches("[0-9]+\\.\\.".toRegex())
        }
    ),

    TEXT({
        true
    }),

    GAMEMODE({ s ->
        listOf("survival", "creative", "adventure", "spectator").any { s.equals(it, true) || "!$s".equals(it, true) }
    }),

    SORT({ s ->
        listOf("nearest", "furthest", "random", "arbitrary").any { s.equals(it, true) }
    })
}