/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.util

import org.bukkit.inventory.meta.*

fun BookMeta.page(text: String): BookMeta {
    addPages(text.component())
    return this
}

fun BookMeta.page(block: ComponentBuilder.() -> Unit): BookMeta {
    addPages(ComponentBuilder().apply(block).build())
    return this
}

fun BookMeta.clear(): BookMeta {
    pages(emptyList())
    return this
}