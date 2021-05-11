/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command.internal

import kotx.minecraft.libs.flylib.command.CommandContext

/**
 * A class that takes arguments.
 * The default send Help refers to this Usage Argument to compose the message.
 *
 * Example:
 * override val usages: List<Usage> = listOf(
 *      Usage(
 *          Argument.Text("input")
 *          "Print user input",
 *          Permission.OP
 *      ) {
 *          sendMessage("You typed: ${args.first()}")
 *      }
 * )
 */
class Usage(
    vararg val args: Argument,
    val description: String = "",
    val permission: Permission? = null,
    val playerOnly: Boolean? = null,
    val options: List<Option> = emptyList(),
    val action: (CommandContext.() -> Unit)? = null
)