/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command.internal

import kotx.minecraft.libs.flylib.command.complete.CompletionContributor

class CommandCompletion(
    val autoCompletion: Boolean,
    val autoSort: Boolean,
    val contributors: List<CompletionContributor>
) {
    class Builder {
        private var autoCompletion = true
        private var autoSort = true
        private val contributors = mutableListOf<CompletionContributor>()

        fun disableAutoCompletion(): Builder {
            autoCompletion = false
            return this
        }

        fun disableAutoSort(): Builder {
            autoSort = false
            return this
        }

        fun registerContributor(vararg contributor: CompletionContributor): Builder {
            contributors.addAll(contributor)
            return this
        }

        fun build() = CommandCompletion(
            autoCompletion, autoSort, contributors.toList()
        )
    }
}