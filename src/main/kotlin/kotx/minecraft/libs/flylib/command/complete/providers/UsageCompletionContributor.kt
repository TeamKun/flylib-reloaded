/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command.complete.providers

import kotx.minecraft.libs.flylib.command.CommandContext
import kotx.minecraft.libs.flylib.command.complete.CompletionContributor

class UsageCompletionContributor : CompletionContributor() {
    override fun suggest(context: CommandContext): List<String> {
        return context.command.usages.mapNotNull {
            it.context.split(" ").getOrNull(context.args.size)
        }.flatMap {
            val templateReg1 = "<(.+?)>".toRegex()
            val templateReg2 = "\\[(.+?)]".toRegex()
            val templateReg3 = "\\((.+?)\\)".toRegex()

            val templateResult = templateReg1.find(it) ?: templateReg2.find(it) ?: templateReg3.find(it)
            val templateContentSplit = templateResult?.groupValues?.get(1)?.split("[/|]".toRegex())

            when {
                it.startsWith("<..") || it.startsWith("[..") || it.startsWith("(..") -> {
                    listOf("...")
                }
                templateContentSplit?.size ?: 0 == 1 -> {
                    val replacements = commandHandler.usageReplacements
                        .filter { it.key(templateResult!!.groupValues[1]) }
                        .flatMap { it.value.invoke(context) }

                    if (replacements.isEmpty()) listOf(it) else replacements
                }
                templateContentSplit?.size ?: 0 > 1 -> templateContentSplit!!.flatMap { s ->
                    val replacements = commandHandler.usageReplacements
                        .filter { it.key(s) }
                        .flatMap { it.value.invoke(context) }

                    if (replacements.isNotEmpty()) replacements else listOf(s)
                }
                else -> listOf(it)
            }
        }
    }

    override fun postProcess(
        currentCompletion: List<String>,
        context: CommandContext
    ): List<String> = if (currentCompletion.isEmpty())
        currentCompletion.filter {
            if (context.args.lastOrNull()
                    .isNullOrBlank()
            ) true else !it.matches("<.+>|\\[.+]|\\(.+\\)|\\.\\.\\.".toRegex())
        }.filter { it.startsWith(context.args.lastOrNull() ?: "", true) }
    else
        currentCompletion.filter { !it.matches("<.+>|\\[.+]|\\(.+\\)|\\.\\.\\.".toRegex()) }
}