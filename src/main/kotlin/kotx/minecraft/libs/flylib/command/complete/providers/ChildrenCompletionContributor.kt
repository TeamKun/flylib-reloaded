package kotx.minecraft.libs.flylib.command.complete.providers

import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandContext
import kotx.minecraft.libs.flylib.command.complete.CompletionContributor

class ChildrenCompletionContributor : CompletionContributor() {
    override fun suggest(command: Command, context: CommandContext): List<String> =
        command.children.map { it.name }

    override fun postProcess(
        currentCompletion: List<String>,
        command: Command,
        context: CommandContext
    ) = currentCompletion.filter { it.startsWith(context.args.lastOrNull() ?: "", true) }
}