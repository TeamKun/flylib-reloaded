package kotx.minecraft.libs.flylib.command.complete.providers

import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandConsumer
import kotx.minecraft.libs.flylib.command.complete.CompletionContributor

class UsageCompletionContributor : CompletionContributor() {
    override fun suggest(command: Command, consumer: CommandConsumer): List<String> {
        return command.usages.mapNotNull {
            it.context.split(" ").getOrNull(consumer.args.size)
        }.flatMap {
            val templateReg1 = "<(.+?)>".toRegex()
            val templateReg2 = "\\[(.+?)]".toRegex()
            val templateReg3 = "\\((.+?)\\)".toRegex()

            val templateResult = templateReg1.find(it) ?: templateReg2.find(it) ?: templateReg3.find(it)
            val templateContentSplit = templateResult?.groupValues?.get(1)?.split("[/|]".toRegex())

            when {
                it.startsWith("<..") || it.startsWith("[..") || it.startsWith("(..") -> listOf("...")
                templateContentSplit?.size ?: 0 == 1 -> {
                    val replacements = commandHandler.usageReplacements
                        .filter { it.key(templateResult!!.groupValues[1]) }
                        .flatMap { it.value.invoke(consumer) }

                    if (replacements.isEmpty()) listOf(it) else replacements
                }
                templateContentSplit?.size ?: 0 > 1 -> templateContentSplit!!.flatMap { s ->
                    val replacements = commandHandler.usageReplacements
                        .filter { it.key(s) }
                        .flatMap { it.value.invoke(consumer) }

                    if (replacements.isNotEmpty()) replacements else listOf(s)
                }
                else -> listOf(it)
            }
        }
    }

    override fun postProcess(
        currentCompletion: List<String>,
        command: Command,
        consumer: CommandConsumer
    ): List<String> =
        currentCompletion.filter {
            if (consumer.args.lastOrNull()
                    .isNullOrBlank()
            ) true else !it.matches("<.+>|\\[.+]|\\(.+\\)|\\.\\.\\.".toRegex())
        }.filter { it.startsWith(consumer.args.lastOrNull() ?: "", true) }
}