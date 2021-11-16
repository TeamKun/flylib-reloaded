/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command

import dev.kotx.flylib.command.arguments.StringArgument
import dev.kotx.flylib.command.parameters.ArrayElement
import dev.kotx.flylib.command.parameters.BooleanElement
import dev.kotx.flylib.command.parameters.DoubleElement
import dev.kotx.flylib.command.parameters.FloatElement
import dev.kotx.flylib.command.parameters.IntegerElement
import dev.kotx.flylib.command.parameters.LongElement
import dev.kotx.flylib.command.parameters.ObjectArrayElement
import dev.kotx.flylib.command.parameters.ObjectElement
import dev.kotx.flylib.command.parameters.StringElement
import dev.kotx.flylib.util.ComponentBuilder
import dev.kotx.flylib.util.component
import java.awt.Color

class Config(
    val elements: List<ConfigElement<*>>
) {
    fun encodeToMap(): Map<String, Any?> = elements.associate {
        it.key to when (it) {
            is ObjectElement -> it.value?.encodeToMap()
            is ObjectArrayElement -> it.value?.map { it.encodeToMap() }
            else -> it.value
        }
    }

    fun asCommand(baseName: String? = null, name: String): Command = object : Command(name) {
        init {
            elements.forEach { element ->
                val fullName = if (baseName == null)
                    element.key
                else
                    "$baseName.${element.key}"

                when (element) {
                    is ObjectElement -> {
                        if (element.value == null)
                            children(object : Command(element.key) {
                                override fun CommandContext.execute() {
                                    if (baseName == null)
                                        fail("The value of ${element.key} is expected to be JsonObject, but is currently set to null.")
                                    else
                                        fail("The value of \"$baseName.${element.key}\" is expected to be JsonObject, but is currently set to null.")
                                }
                            })
                        else
                            children(
                                element.value.asCommand(
                                    if (baseName == null) element.key else "$baseName.${element.key}",
                                    element.key
                                )
                            )
                    }

                    is ArrayElement<*> -> {
                        children(object : Command(element.key) {
                            init {
                                usage {
                                    literalArgument("get")
                                    executes {
                                        message {
                                            bold("[!] ", Color.BLUE)
                                            append("\"$fullName\"", Color(180, 85, 227))
                                            append(": ", Color(147, 52, 25))
                                            if (element.value == null) {
                                                append("null", Color(222, 72, 26))
                                            } else {
                                                append("[")
                                                element.value!!.forEachIndexed { index, value ->
                                                    append(value.toComponent())

                                                    if (index < element.value!!.size - 1)
                                                        append(", ", Color(147, 52, 25))
                                                }
                                                append("]")
                                            }
                                        }
                                    }
                                }

                                usage {
                                    literalArgument("add")
                                    stringArgument("value(s)", StringArgument.Type.PHRASE)

                                    executes {

                                    }
                                }

                                usage {
                                    literalArgument("remove")
                                    stringArgument("value(s)", StringArgument.Type.PHRASE, suggestion = {

                                    })

                                    executes {

                                    }
                                }

                                usage {
                                    literalArgument("clear")

                                    executes {

                                    }
                                }
                            }
                        })
                    }
                    else -> {
                        children(object : Command(element.key) {
                            init {
                                usage {
                                    literalArgument("get")
                                    executes {
                                        message {
                                            bold("[!] ", Color.BLUE)
                                            append("\"$fullName\"", Color(180, 85, 227))
                                            append(": ", Color(147, 52, 25))
                                            if (element.value == null) {
                                                append("null", Color(222, 72, 26))
                                            } else {
                                                append(element.value.toComponent())
                                            }
                                        }
                                    }
                                }

                                usage {
                                    literalArgument("set")

                                    when (element) {
                                        is IntegerElement -> integerArgument("value")
                                        is LongElement -> longArgument("value")
                                        is FloatElement -> floatArgument("value")
                                        is DoubleElement -> doubleArgument("value")
                                        is StringElement -> stringArgument("value")
                                        is BooleanElement -> booleanArgument("value")
                                    }

                                    executes {
                                        if (baseName == null)
                                            success("Set primitive value of \"${element.key}\" to ${args.first()}")
                                        else
                                            success("Set primitive value of \"$baseName ${element.key}\" to ${args.first()}")
                                    }
                                }
                            }
                        })
                    }
                }
            }

            usage {
                literalArgument("get")
                executes {
                    message {
                        bold("[!] ", Color.BLUE)
                        append(if (baseName == null) "root" else "\"$baseName\"", Color(180, 85, 227))
                        appendln(": ", Color(147, 52, 25))
                        appendln("{")
                        asJson(1, elements)
                        appendln("}")
                        appendln()
                    }
                }
            }
        }
    }

    private fun ComponentBuilder.asJson(depth: Int, elements: List<ConfigElement<*>>) {
        val tabSize = "  ".repeat(depth)
        elements.forEachIndexed { index, element ->
            when (element) {
                is ObjectElement -> {
                    append(tabSize)
                    append("\"${element.key}\"", Color(180, 85, 227))
                    append(": ", Color(147, 52, 25))
                    appendln("{")
                    asJson(depth + 1, element.value!!.elements)
                    append("$tabSize}")
                    if (index < elements.size - 1)
                        appendln(",", Color(147, 52, 25))
                    else
                        appendln()
                }
                is ArrayElement<*> -> {
                    append(tabSize)
                    append("\"${element.key}\"", Color(180, 85, 227))
                    append(": ", Color(147, 52, 25))
                    appendln("[")
                    element.value!!.forEachIndexed { index, it ->
                        append("$tabSize  ")
                        append(it.toComponent())
                        if (index < element.value!!.size - 1)
                            appendln(",", Color(147, 52, 25))
                        else
                            appendln()
                    }
                    append("$tabSize]")
                    if (index < elements.size - 1)
                        appendln(",", Color(147, 52, 25))
                    else
                        appendln()
                }
                else -> {
                    append(tabSize)
                    append("\"${element.key}\"", Color(180, 85, 227))
                    append(": ", Color(147, 52, 25))
                    append(element.value.toComponent())
                    if (index < elements.size - 1)
                        appendln(",", Color(147, 52, 25))
                    else
                        appendln()
                }
            }
        }
    }

    private fun Any?.toComponent() = when (this) {
        is String -> "\"${toString()}\"".component(Color(83, 184, 59))
        is Number -> toString().component(Color(58, 165, 239))
        else -> toString().component(Color(255, 118, 76))
    }
}