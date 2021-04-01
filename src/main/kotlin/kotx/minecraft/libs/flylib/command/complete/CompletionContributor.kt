package kotx.minecraft.libs.flylib.command.complete

import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandConsumer
import kotx.minecraft.libs.flylib.command.CommandHandler
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class CompletionContributor : KoinComponent {
    protected val commandHandler by inject<CommandHandler>()

    open fun suggest(command: Command, consumer: CommandConsumer): List<String> = emptyList()
    open fun postProcess(currentCompletion: List<String>, command: Command, consumer: CommandConsumer): List<String> =
        currentCompletion
}