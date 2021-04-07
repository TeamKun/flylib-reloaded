package kotx.minecraft.libs.flylib.command.complete.providers

import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandConsumer
import kotx.minecraft.libs.flylib.command.complete.CompletionContributor

class OptionCompletionContributor : CompletionContributor() {
    override fun suggest(command: Command, consumer: CommandConsumer): List<String> {
        return command.usages.filter {
            it.context.split(" ").size > consumer.args.size
        }.flatMap {
            it.options.map { it.name } + it.options.flatMap { it.aliases }
        }.map {
            if (it.length == 1) "-$it" else "--$it"
        }
    }
}