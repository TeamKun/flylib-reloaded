package kotx.minecraft.libs.flylib.command.complete.providers

import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandConsumer
import kotx.minecraft.libs.flylib.command.complete.CompletionContributor

class ChildrenCompletionContributor : CompletionContributor() {
    override fun suggest(command: Command, consumer: CommandConsumer): List<String> =
        command.children.map { it.name }

    override fun postProcess(
        currentCompletion: List<String>,
        command: Command,
        consumer: CommandConsumer
    ) = currentCompletion.filter { it.startsWith(consumer.args.lastOrNull() ?: "", true) }
}