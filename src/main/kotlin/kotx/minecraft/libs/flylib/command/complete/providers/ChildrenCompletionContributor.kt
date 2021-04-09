/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command.complete.providers

import kotx.minecraft.libs.flylib.command.CommandContext
import kotx.minecraft.libs.flylib.command.complete.CompletionContributor

class ChildrenCompletionContributor : CompletionContributor() {
    override fun suggest(context: CommandContext): List<String> =
        context.command.children.map { it.name }

    override fun postProcess(
        currentCompletion: List<String>,
        context: CommandContext
    ) = currentCompletion.filter { it.startsWith(context.args.lastOrNull() ?: "", true) }
}