/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.*
import java.awt.*

class ComponentBuilder {
    private val component = Component.text()

    fun append(text: String): ComponentBuilder {
        component.append(text.component())
        return this
    }

    fun append(text: String, color: Color): ComponentBuilder {
        component.append(text.component(Style.style(TextColor.color(color.rgb))))
        return this
    }

    fun append(text: String, vararg decorations: TextDecoration): ComponentBuilder {
        component.append(text.component(Style.style(*decorations)))
        return this
    }

    fun append(text: String, color: Color, vararg decorations: TextDecoration): ComponentBuilder {
        component.append(text.component(Style.style(*decorations, TextColor.color(color.rgb))))
        return this
    }

    fun appendln(text: String): ComponentBuilder {
        component.append("$text\n".component())
        return this
    }

    fun appendln(text: String, color: Color): ComponentBuilder {
        component.append("$text\n".component(Style.style(TextColor.color(color.rgb))))
        return this
    }

    fun appendln(text: String, vararg decorations: TextDecoration): ComponentBuilder {
        component.append("$text\n".component(Style.style(*decorations)))
        return this
    }

    fun appendln(text: String, color: Color, vararg decorations: TextDecoration): ComponentBuilder {
        component.append("$text\n".component(Style.style(*decorations, TextColor.color(color.rgb))))
        return this
    }

    fun bold(text: String): ComponentBuilder {
        component.append(text.component())
        return this
    }

    fun bold(text: String, color: Color): ComponentBuilder {
        component.append(text.component(Style.style(TextColor.color(color.rgb), TextDecoration.BOLD)))
        return this
    }

    fun bold(text: String, vararg decorations: TextDecoration): ComponentBuilder {
        component.append(text.component(Style.style(*decorations, TextDecoration.BOLD)))
        return this
    }

    fun bold(text: String, color: Color, vararg decorations: TextDecoration): ComponentBuilder {
        component.append(text.component(Style.style(*decorations, TextDecoration.BOLD, TextColor.color(color.rgb))))
        return this
    }

    fun boldln(text: String): ComponentBuilder {
        component.append("$text\n".component())
        return this
    }

    fun boldln(text: String, color: Color): ComponentBuilder {
        component.append("$text\n".component(Style.style(TextColor.color(color.rgb), TextDecoration.BOLD)))
        return this
    }

    fun boldln(text: String, vararg decorations: TextDecoration): ComponentBuilder {
        component.append("$text\n".component(Style.style(*decorations, TextDecoration.BOLD)))
        return this
    }

    fun boldln(text: String, color: Color, vararg decorations: TextDecoration): ComponentBuilder {
        component.append("$text\n".component(Style.style(*decorations, TextDecoration.BOLD, TextColor.color(color.rgb))))
        return this
    }

    operator fun String.unaryPlus() {
        component.append(this.component())
    }

    operator fun Component.unaryPlus() {
        component.append(this)
    }

    internal fun build() = component.build()
}