package kotx.minecraft.libs.flylib.command.complete.providers

import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandConsumer
import kotx.minecraft.libs.flylib.command.complete.CompletionContributor

class BasicCompletionContributor : CompletionContributor() {
    override fun postProcess(
        currentCompletion: List<String>,
        command: Command,
        consumer: CommandConsumer
    ): List<String> =
        currentCompletion.filter { it.startsWith(consumer.args.lastOrNull() ?: "") }
}