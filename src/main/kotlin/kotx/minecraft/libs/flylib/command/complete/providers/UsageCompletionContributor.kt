/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command.complete.providers

import kotx.minecraft.libs.flylib.command.CommandContext
import kotx.minecraft.libs.flylib.command.complete.CompletionContributor

class UsageCompletionContributor(
    private val onlyCompleteIfEmpty: Boolean = true,
    private val usageReplacements: Map<(String) -> Boolean, CommandContext.(String) -> List<String>> = emptyMap()
) : CompletionContributor() {
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
                    val replacements = usageReplacements
                        .filter { it.key(templateResult!!.groupValues[1]) }
                        .flatMap { it.value.invoke(context, templateResult!!.groupValues[1]) }

                    replacements.ifEmpty { templateContentSplit }
                }
                templateContentSplit?.size ?: 0 > 1 -> templateContentSplit!!.flatMap { s ->
                    val replacements = usageReplacements
                        .filter { it.key(s) }
                        .flatMap { it.value.invoke(context, s) }

                    replacements.ifEmpty { listOf(s) }
                }
                else -> listOf(it)
            }
        }
    }

    override fun postProcess(
        currentCompletion: List<String>, selfCompletion: List<String>, context: CommandContext
    ): List<String> = if (!onlyCompleteIfEmpty || (onlyCompleteIfEmpty && currentCompletion.isEmpty())) {
        selfCompletion.filter {
            context.args.lastOrNull().isNullOrBlank()
        }.filter {
            it.startsWith(context.args.lastOrNull() ?: "", true)
        } + currentCompletion
    } else currentCompletion
}