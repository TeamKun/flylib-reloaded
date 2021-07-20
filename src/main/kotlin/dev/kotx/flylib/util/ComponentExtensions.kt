/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import java.awt.Color

/**
 * Converts a String to a Text Component.
 * You can also specify the Style.
 */
fun String.component(style: Style = Style.empty()) = Component.text(this, style)

/**
 * Converts a String to a Text Component.
 * You can also specify the Color.
 */
fun String.component(color: Color) = Component.text(this, Style.style(TextColor.color(color.rgb)))

/**
 * Create a TextComponent using ComponentBuilder.
 */
fun component(builder: ComponentBuilderAction) = ComponentBuilder().apply { builder.apply { initialize() } }.build()