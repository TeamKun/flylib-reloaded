/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import java.awt.Color

/**
 * A builder that makes components easy to use from Kotlin.
 */
class ComponentBuilder {
    private val component = Component.text()

    /**
     * Add text.
     */
    fun append(text: String): ComponentBuilder {
        component.append(text.component())
        return this
    }

    /**
     * Add text.
     */
    fun append(component: Component): ComponentBuilder {
        component.append(component)
        return this
    }

    /**
     * Adds text with a specified color.
     */
    fun append(text: String, color: Color): ComponentBuilder {
        component.append(text.component(Style.style(TextColor.color(color.rgb))))
        return this
    }

    /**
     * Add text with decoration.
     */
    fun append(text: String, vararg decorations: TextDecoration): ComponentBuilder {
        component.append(text.component(Style.style(*decorations)))
        return this
    }

    /**
     * Adds text with color, decorations
     */
    fun append(text: String, color: Color, vararg decorations: TextDecoration): ComponentBuilder {
        component.append(text.component(Style.style(*decorations, TextColor.color(color.rgb))))
        return this
    }

    /**
     * Adds line break.
     */
    fun appendln(): ComponentBuilder {
        component.append("\n".component())
        return this
    }

    /**
     * Adds text with line break.
     */
    fun appendln(text: String): ComponentBuilder {
        component.append("$text\n".component())
        return this
    }

    /**
     * Adds colored text with line breaks.
     */
    fun appendln(text: String, color: Color): ComponentBuilder {
        component.append("$text\n".component(Style.style(TextColor.color(color.rgb))))
        return this
    }

    /**
     * Adds decorated text with line breaks.
     */
    fun appendln(text: String, vararg decorations: TextDecoration): ComponentBuilder {
        component.append("$text\n".component(Style.style(*decorations)))
        return this
    }

    /**
     * Adds decorated colored text with line breaks.
     */
    fun appendln(text: String, color: Color, vararg decorations: TextDecoration): ComponentBuilder {
        component.append("$text\n".component(Style.style(*decorations, TextColor.color(color.rgb))))
        return this
    }

    /**
     * Adds bold text.
     */
    fun bold(text: String): ComponentBuilder {
        component.append(text.component())
        return this
    }

    /**
     * Adds bold text with color.
     */
    fun bold(text: String, color: Color): ComponentBuilder {
        component.append(text.component(Style.style(TextColor.color(color.rgb), TextDecoration.BOLD)))
        return this
    }

    /**
     * Adds bold text with decorations
     */
    fun bold(text: String, vararg decorations: TextDecoration): ComponentBuilder {
        component.append(text.component(Style.style(*decorations, TextDecoration.BOLD)))
        return this
    }

    /**
     * Adds bold text with color, decorations
     */
    fun bold(text: String, color: Color, vararg decorations: TextDecoration): ComponentBuilder {
        component.append(text.component(Style.style(*decorations, TextDecoration.BOLD, TextColor.color(color.rgb))))
        return this
    }

    /**
     * Adds bold text with line breaks.
     */
    fun boldln(text: String): ComponentBuilder {
        component.append("$text\n".component())
        return this
    }

    /**
     * Adds colored bold text with line breaks.
     */
    fun boldln(text: String, color: Color): ComponentBuilder {
        component.append("$text\n".component(Style.style(TextColor.color(color.rgb), TextDecoration.BOLD)))
        return this
    }

    /**
     * Adds decorated bold text with line breaks.
     */
    fun boldln(text: String, vararg decorations: TextDecoration): ComponentBuilder {
        component.append("$text\n".component(Style.style(*decorations, TextDecoration.BOLD)))
        return this
    }

    /**
     * Adds decorated colored text with line breaks.
     */
    fun boldln(text: String, color: Color, vararg decorations: TextDecoration): ComponentBuilder {
        component.append(
            "$text\n".component(
                Style.style(
                    *decorations,
                    TextDecoration.BOLD,
                    TextColor.color(color.rgb)
                )
            )
        )
        return this
    }

    /**
     * Adds text with unaryPlus
     */
    operator fun String.unaryPlus() {
        component.append(this.component())
    }

    /**
     * Adds component with unaryPlus
     */
    operator fun Component.unaryPlus() {
        component.append(this)
    }

    /**
     * Adds colored text
     */
    infix fun String.color(color: Color) {
        append(this, color)
    }

    /**
     * Adds colored text with line break
     */
    infix fun String.boldColor(color: Color) {
        bold(this, color)
    }

    /**
     * Adds colored bold text
     */
    infix fun String.colorln(color: Color) {
        appendln(this, color)
    }

    /**
     * Adds colored bold text wit hline break
     */
    infix fun String.boldColorln(color: Color) {
        boldln(this, color)
    }

    internal fun build() = component.build()
}

/**
 * ComponentBuilder's builder lambda function.
 */
fun interface ComponentBuilderAction {
    /**
     * A lambda expression for the component builder.
     */
    fun ComponentBuilder.initialize()
}