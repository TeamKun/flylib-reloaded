package kotx.minecraft.libs.flylib.command.complete

import kotx.minecraft.libs.flylib.command.Command
import kotx.minecraft.libs.flylib.command.CommandConsumer
import kotx.minecraft.libs.flylib.command.CommandHandler
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * A class that provides tab completion for commands.
 * It has suggest(command: Command, consumer: CommandConsumer): List<String> and
 * postProcess(currentCompletion: List<String>, command: Command, consumer: CommandConsumer): List<String>, each of which corresponds to autoTabSelect and autoTabCompletion of
 * Each of them corresponds to autoTabSelect and autoTabCompletion of CommandHandler.
 *
 * If these parameters are valid, the corresponding methods will be executed in the following order.
 * If AContributor and BContributor are present:
 * The result is
 * 1. Add all the List<String> provided by AContributor#suggest, and
 * 2. Overwritten by the output of AContributor#postProcess, and
 * 3. If there is a BContributor: result will add all of the List<String> provided by AContributor#suggest, and will be overwritten by the output of AContributor#postProcess.
 * 4. All the List<String> provided by BContributor#suggest are added and overwritten in the output of BContributor#postProcess.
 */
open class CompletionContributor : KoinComponent {
    protected val commandHandler by inject<CommandHandler>()

    /**
     * A method that provides a list of tab completions.
     * Executed only when auto Tab Completion of Command Handler is true.
     *
     * @param command Command to perform tab completion
     * @param consumer Context to perform tab completion, player and arguments running, server, etc.
     *
     * @return Tab completion list you want to add
     */
    open fun suggest(command: Command, consumer: CommandConsumer): List<String> = emptyList()

    /**
     * A method for adding, editing, filtering, etc. to the current suggest.
     * Executed only when auto Tab Select of Command Handler is true.
     *
     * @param currentCompletion Current tab completion list
     * @param command Command to perform tab completion
     * @param consumer Context to perform tab completion, player and arguments running, server, etc.
     *
     * @return The result of filtering the current Completion
     */
    open fun postProcess(currentCompletion: List<String>, command: Command, consumer: CommandConsumer): List<String> =
        currentCompletion
}