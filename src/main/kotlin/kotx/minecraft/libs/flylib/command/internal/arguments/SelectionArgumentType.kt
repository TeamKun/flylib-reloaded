/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command.internal.arguments

import com.mojang.brigadier.LiteralMessage
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.server.v1_16_R3.CommandListenerWrapper
import java.util.concurrent.CompletableFuture

class SelectionArgumentType private constructor(private val selections: List<String>) : ArgumentType<String> {
    companion object {
        fun selection(selections: List<String>) = SelectionArgumentType(selections)
        fun getString(context: CommandContext<CommandListenerWrapper>, name: String?) = context.getArgument(name, String::class.java) as String
    }

    @Throws(CommandSyntaxException::class)
    override fun parse(reader: StringReader): String {
        val start = reader.cursor
        val result = reader.readString()

        if (selections.none { it.equals(result, true) }) {
            reader.cursor = start
            throw Dynamic2CommandExceptionType { found: Any, selectors: Any ->
                LiteralMessage("Must be one of [${(selectors as List<String>).joinToString()}]. input:$found")
            }.createWithContext(reader, result, this.selections)
        }

        return result
    }

    override fun <S : Any?> listSuggestions(context: CommandContext<S>, builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
        selections.filter { it.startsWith(builder.remaining, true) }.forEach { builder.suggest(it) }
        return builder.buildFuture()
    }

    override fun toString(): String = "selections(${selections.joinToString()})"

    override fun getExamples(): MutableCollection<String> = mutableListOf("add", "remove", "0", "1", "+", "-")
}