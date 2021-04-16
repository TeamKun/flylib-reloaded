/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command.internal

import kotx.minecraft.libs.flylib.command.complete.CompletionContributor

class CommandCompletion(
    val contributors: List<CompletionContributor>
) {
    class Builder {
        private val contributors = mutableListOf<CompletionContributor>()

        /**
         * タブ保管をサポートするCompletionContributorのインスタンスを追加します。CompletionContributorの詳細については以下のJavadocを参照してください。
         * @see CompletionContributor
         */
        fun register(vararg contributor: CompletionContributor): Builder {
            contributors.addAll(contributor)
            return this
        }

        fun build() = CommandCompletion(
            contributors.toList()
        )
    }
}