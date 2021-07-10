/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.Style

fun String.component(style: Style = Style.empty()) = Component.text(this, style)

fun component(builder: ComponentBuilder.() -> Unit) = ComponentBuilder().apply(builder).build()