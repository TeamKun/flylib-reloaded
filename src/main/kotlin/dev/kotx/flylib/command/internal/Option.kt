/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.command.internal

data class Option @JvmOverloads constructor(
    val name: String,
    val description: String = "",
    val aliases: List<String> = emptyList(),
    val required: Boolean = false
)