/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.util

import org.bukkit.inventory.meta.BookMeta

/**
 * Adds page with text
 */
fun BookMeta.page(text: String): BookMeta {
    addPages(text.component())
    return this
}

/**
 * Adds page using component builder
 */
fun BookMeta.page(block: ComponentBuilderAction): BookMeta {
    addPages(ComponentBuilder().apply { block.apply { initialize() } }.build())
    return this
}

/**
 * Clear all pages.
 */
fun BookMeta.clear(): BookMeta {
    pages(emptyList())
    return this
}