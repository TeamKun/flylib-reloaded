/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command.internal

class Usage(
    vararg val args: Argument,
    val description: String = "",
    val options: List<Option> = emptyList()
)