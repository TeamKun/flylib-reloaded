/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.Style

/**
 * Converts a String to a Text Component.
 * You can also specify the Style.
 */
fun String.component(style: Style = Style.empty()) = Component.text(this, style)

/**
 * Create a TextComponent using ComponentBuilder.
 */
fun component(builder: ComponentBuilder.() -> Unit) = ComponentBuilder().apply(builder).build()