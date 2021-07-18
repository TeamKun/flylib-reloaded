/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.util

import dev.kotx.flylib.command.Command

internal val Command.fullCommand: List<Command>
    get() {
        val commands = mutableListOf<Command>()
        var current: Command? = this

        do {
            commands.add(current!!)
            current = current.parent
        } while (current != null)

        return commands.reversed()
    }

internal fun <T> List<T>.joint(other: T): List<T> {
    val res = mutableListOf<T>()
    forEachIndexed { i, it ->
        res.add(it)
        if (i < size - 1)
            res.add(other)
    }

    return res.toList()
}

internal fun <T, E> List<T>.joint(joiner: E, target: (T) -> E) = map(target).joint(joiner)