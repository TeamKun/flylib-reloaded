/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command

/**
 * This is an option for the command. (A command option that starts with -- or - in linux)
 */
data class Option(
    val name: String,
    val description: String = "",
    val aliases: List<String> = emptyList(),
    val required: Boolean = false
)