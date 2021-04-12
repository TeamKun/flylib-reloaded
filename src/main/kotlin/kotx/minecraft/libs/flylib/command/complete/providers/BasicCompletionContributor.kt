/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command.complete.providers

import kotx.minecraft.libs.flylib.command.CommandContext
import kotx.minecraft.libs.flylib.command.complete.CompletionContributor

class BasicCompletionContributor : CompletionContributor() {
    override fun postProcess(currentCompletion: List<String>, selfCompletion: List<String>, context: CommandContext) =
        currentCompletion.distinct().filter { it.startsWith(context.args.lastOrNull() ?: "") }
}