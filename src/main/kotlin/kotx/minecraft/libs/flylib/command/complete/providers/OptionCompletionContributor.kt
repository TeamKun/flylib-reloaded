/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command.complete.providers

import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandContext
import kotx.minecraft.libs.flylib.command.complete.CompletionContributor

class OptionCompletionContributor : CompletionContributor() {
    override fun suggest(command: Command, context: CommandContext): List<String> {
        return command.usages.filter {
            context.args.size >= it.context.split(" ").size
        }.flatMap {
            it.options.map { it.name } + it.options.flatMap { it.aliases }
        }.map {
            if (it.length == 1) "-$it" else "--$it"
        }
    }

    override fun postProcess(currentCompletion: List<String>, command: Command, context: CommandContext): List<String> {
        val lastOption = "-([a-z]+)".toRegex(RegexOption.IGNORE_CASE).matchEntire(context.args.lastOrNull() ?: "")
            ?: return currentCompletion.filter { it.startsWith(context.args.lastOrNull() ?: "", true) }
        return currentCompletion.mapNotNull { "-([a-z])".toRegex(RegexOption.IGNORE_CASE).matchEntire(it) }
            .filter { r ->
                lastOption.groupValues[1].none { it.toString() == r.groupValues[1] }
            }.map {
                "-${lastOption.groupValues[1]}${it.groupValues[1]}"
            }
    }
}