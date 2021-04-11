/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command.complete.providers

import kotx.minecraft.libs.flylib.command.CommandContext
import kotx.minecraft.libs.flylib.command.complete.CompletionContributor

class OptionCompletionContributor : CompletionContributor() {
    override fun suggest(context: CommandContext): List<String> {
        val existOptions = context.args.flatMap {
            when {
                it.startsWith("--") -> listOf(it.drop(2))
                it.startsWith("-") -> it.drop(1).toList().map { it.toString() }
                else -> emptyList()
            }
        }

        return context.command.usages.filter {
            context.args.size >= it.context.split(" ").size
        }.flatMap {
            it.options.map { it.name } + it.options.flatMap { it.aliases }
        }.map {
            if (it.length == 1) "-$it" else "--$it"
        }.filterNot {
            existOptions.any { op ->
                val opt = if (op.length == 1)
                    "-$op"
                else
                    "--$op"
                opt == it
            }
        }
    }

    override fun postProcess(currentCompletion: List<String>, selfCompletion: List<String>, context: CommandContext): List<String> {
        val lastOption = "-([a-z]+)".toRegex(RegexOption.IGNORE_CASE).matchEntire(context.args.lastOrNull() ?: "")
            ?: return selfCompletion.filter { it.startsWith(context.args.lastOrNull() ?: "", true) } + currentCompletion
        val lastOpt = lastOption.groupValues[1]
        return selfCompletion.mapNotNull {
            "-([a-z])".toRegex(RegexOption.IGNORE_CASE).matchEntire(it)
        }.filter { r ->
            lastOpt.none { it.toString() == r.groupValues[1] }
        }.map {
            "-${lastOpt}${it.groupValues[1]}"
        } + currentCompletion
    }
}