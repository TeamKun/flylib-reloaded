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

    @JvmOverloads
    fun append(text: String, color: Color = Color.WHITE, vararg decorations: TextDecoration = emptyArray(), style: Style.Builder.() -> Unit = {}): TextComponentBuilder {
        textComponentBuilder.append(text.component(color, *decorations) { apply(style) })
        return this
    }

    fun append(text: String, vararg decorations: TextDecoration = emptyArray(), style: Style.Builder.() -> Unit = {}): TextComponentBuilder {
        textComponentBuilder.append(text.component(*decorations) { apply(style) })
        return this
    }

    fun append(text: String, style: Style): TextComponentBuilder {
        textComponentBuilder.append(text.component(style))
        return this
    }

    @JvmOverloads
    fun bold(text: String, color: Color = Color.WHITE, vararg decorations: TextDecoration = emptyArray(), style: Style.Builder.() -> Unit = {}): TextComponentBuilder {
        textComponentBuilder.append(text.component(color, *decorations) {
            decorate(TextDecoration.BOLD)
            apply(style)
        })
        return this
    }

    fun bold(text: String, vararg decorations: TextDecoration = emptyArray(), style: Style.Builder.() -> Unit = {}): TextComponentBuilder {
        textComponentBuilder.append(text.component(*decorations) {
            decorate(TextDecoration.BOLD)
            apply(style)
        })
        return this
    }

    fun bold(text: String, style: Style): TextComponentBuilder {
        textComponentBuilder.append(text.component(style.apply { this.decorate(TextDecoration.BOLD) }))
        return this
    }

    fun appendln(): TextComponentBuilder {
        textComponentBuilder.append("\n".component())
        return this
    }

    @JvmOverloads
    fun appendln(text: String, color: Color = Color.WHITE, vararg decorations: TextDecoration = emptyArray(), style: Style.Builder.() -> Unit = {}): TextComponentBuilder {
        textComponentBuilder.append("$text\n".component(color, *decorations) { apply(style) })
        return this
    }

    fun appendln(text: String, vararg decorations: TextDecoration = emptyArray(), style: Style.Builder.() -> Unit = {}): TextComponentBuilder {
        textComponentBuilder.append("$text\n".component(*decorations) { apply(style) })
        return this
    }

    fun appendln(text: String, style: Style): TextComponentBuilder {
        textComponentBuilder.append("$text\n".component(style))
        return this
    }

    @JvmOverloads
    fun boldln(text: String, color: Color = Color.WHITE, vararg decorations: TextDecoration = emptyArray(), style: Style.Builder.() -> Unit = {}): TextComponentBuilder {
        textComponentBuilder.append("$text\n".component(color, *decorations) {
            decorate(TextDecoration.BOLD)
            apply(style)
        })
        return this
    }

    fun boldln(text: String, vararg decorations: TextDecoration = emptyArray(), style: Style.Builder.() -> Unit = {}): TextComponentBuilder {
        textComponentBuilder.append("$text\n".component(*decorations) {
            decorate(TextDecoration.BOLD)
            apply(style)
        })
        return this
    }

    fun boldln(text: String, style: Style): TextComponentBuilder {
        textComponentBuilder.append("$text\n".component(style.apply { this.decorate(TextDecoration.BOLD) }))
        return this
    }

    fun append(component: Component): TextComponentBuilder {
        textComponentBuilder.append(component)
        return this
    }

    operator fun String.unaryPlus() {
        append(this)
    }

    operator fun Component.unaryPlus() {
        append(this)
    }

    fun build() = textComponentBuilder.build()
}

operator fun Component.plus(component: Component) = this.append(component)