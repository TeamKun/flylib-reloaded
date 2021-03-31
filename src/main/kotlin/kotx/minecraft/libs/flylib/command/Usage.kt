/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command

/**
 * This is how to use commands.
 * ## Examples
 * Consider the case where there is a command called BanCommand.
 * Add the following to BanCommand.
 * ```kotlin
 * usages = listOf(
 *     Usage(
 *         context = "ban <user>",
 *         description = "ban a user"
 *     )
 * )
 * ```
 *
 * Consider the case where there is a command called BlackListCommand, and AddCommand and RemoveCommand exist as child elements of it.
 * If BlackListCommand does not take any arguments other than subcommands, the usages of BlackListCommand should be empty.
 * Instead, add the following to AddCommand.
 * ```kotlin
 * usages = listOf(
 *     Usage(
 *         context = "add <user>",
 *         description = "Add blacklist of user",
 *     )
 * )
 * ```
 * Add the following to the RemoveCommand.
 * ```kotlin
 * usages = listOf(
 *     Usage(
 *         context = "add <user>",
 *         description = "Add blacklist of user",
 *     )
 * )
 * ```
 * By doing so, the RemoveCommand usages will not be displayed when using sendHelp for AddCommand, and the usages for both AddCommand and RemoveCommand will only be displayed when using sendHelp for BlackListCommand. usages for both AddCommand and RemoveCommand only when using sendHelp for BlackListCommand.
 */
data class Usage(
    val context: String,
    val description: String = "",
    val options: List<Option> = emptyList()
)