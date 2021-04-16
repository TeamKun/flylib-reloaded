/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package kotx.minecraft.libs.flylib.command.complete.providers

import kotx.minecraft.libs.flylib.command.CommandContext
import kotx.minecraft.libs.flylib.command.complete.CompletionContributor
import java.util.*

class BasicCompletionContributor(
    private val useDistance: Boolean = false
) : CompletionContributor() {
    override fun postProcess(currentCompletion: List<String>, selfCompletion: List<String>, context: CommandContext) = if (useDistance)
        currentCompletion
            .map { it to calculate(it, context.args.lastOrNull() ?: "") }
            .sortedBy { it.second }
            .let {
                when {
                    context.args.lastOrNull().isNullOrBlank() -> it
                    it.size >= 3 -> it.subList(0, 3)
                    else -> it
                }
            }
            .map { it.first }
    else
        currentCompletion.distinct().filter { it.startsWith(context.args.lastOrNull() ?: "") }

    private fun calculate(x: String, y: String): Int {
        val dp = Array(x.length + 1) { IntArray(y.length + 1) }
        for (i in 0..x.length) for (j in 0..y.length) when {
            i == 0 -> dp[i][j] = j
            j == 0 -> dp[i][j] = i
            else -> dp[i][j] = min(
                dp[i - 1][j - 1] + costOfSubstitution(x[i - 1], y[j - 1]),
                dp[i - 1][j] + 1,
                dp[i][j - 1] + 1
            )
        }
        return dp[x.length][y.length]
    }

    private fun costOfSubstitution(a: Char, b: Char): Int {
        return if (a == b) 0 else 1
    }

    private fun min(vararg numbers: Int): Int {
        return Arrays.stream(numbers)
            .min().orElse(Int.MAX_VALUE)
    }
}