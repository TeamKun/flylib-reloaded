/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.utils.component

import dev.kotx.flylib.utils.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.*
import java.awt.*

class TextComponentBuilder {
    private val textComponentBuilder = Component.text()

    fun append(text: String): TextComponentBuilder {
        textComponentBuilder.append(text)
        return this
    }

    fun append(text: String, color: Color): TextComponentBuilder {
        textComponentBuilder.append(text, color)
        return this
    }

    fun append(text: String, decoration: TextDecoration): TextComponentBuilder {
        textComponentBuilder.append(text, decoration)
        return this
    }

    fun append(text: String, style: Style): TextComponentBuilder {
        textComponentBuilder.append(text, style)
        return this
    }

    @JvmOverloads
    fun bold(text: String, vararg decorations: TextDecoration = emptyArray()): TextComponentBuilder {
        textComponentBuilder.append(text, Style.style(TextDecoration.BOLD))
        return this
    }

    @JvmOverloads
    fun bold(text: String, color: Color, vararg decorations: TextDecoration = emptyArray()): TextComponentBuilder {
        textComponentBuilder.append(text, Style.style(TextColor.color(color.rgb), TextDecoration.BOLD, *decorations))
        return this
    }

    fun appendln(text: String): TextComponentBuilder {
        textComponentBuilder.appendln(text)
        return this
    }

    fun appendln(text: String, color: Color): TextComponentBuilder {
        textComponentBuilder.appendln(text, color)
        return this
    }

    fun appendln(text: String, decoration: TextDecoration): TextComponentBuilder {
        textComponentBuilder.appendln(text, decoration)
        return this
    }

    fun appendln(text: String, style: Style): TextComponentBuilder {
        textComponentBuilder.appendln(text, style)
        return this
    }

    @JvmOverloads
    fun boldln(text: String, vararg decorations: TextDecoration = emptyArray()): TextComponentBuilder {
        textComponentBuilder.appendln(text, Style.style(TextDecoration.BOLD))
        return this
    }

    @JvmOverloads
    fun boldln(text: String, color: Color, vararg decorations: TextDecoration = emptyArray()): TextComponentBuilder {
        textComponentBuilder.appendln(text, Style.style(TextColor.color(color.rgb), TextDecoration.BOLD, *decorations))
        return this
    }

    operator fun String.unaryPlus() {
        textComponentBuilder.append(this)
    }

    operator fun Component.unaryPlus() {
        textComponentBuilder.append(this)
    }

    fun build() = textComponentBuilder.build()
}