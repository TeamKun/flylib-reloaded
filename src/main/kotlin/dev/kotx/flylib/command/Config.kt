package dev.kotx.flylib.command

import dev.kotx.flylib.command.arguments.TextArgument
import dev.kotx.flylib.command.parameters.ArrayElement
import dev.kotx.flylib.command.parameters.BooleanElement
import dev.kotx.flylib.command.parameters.DoubleElement
import dev.kotx.flylib.command.parameters.FloatElement
import dev.kotx.flylib.command.parameters.IntegerElement
import dev.kotx.flylib.command.parameters.LongElement
import dev.kotx.flylib.command.parameters.ObjectArrayElement
import dev.kotx.flylib.command.parameters.ObjectElement
import dev.kotx.flylib.command.parameters.StringElement

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
            elements.forEach { parameter ->
                when (parameter) {
                    is ObjectElement -> {
                        if (parameter.value == null)
                            children(object : Command(parameter.key) {
                                override fun CommandContext.execute() {
                                    //
                                    fail("The value of \"$baseName ${parameter.key}\" is expected to be JsonObject, but is currently set to null.")
                                }
                            })
                        else
                            children(parameter.value.asCommand(if (baseName == null) parameter.key else "$baseName ${parameter.key}", parameter.key))
                    }

                    is ObjectArrayElement -> {

                    }

                    is ArrayElement<*> -> {
                        children(object : Command(parameter.key) {
                            init {
                                usage {
                                    literalArgument("add")
                                    stringArgument("value(s)", TextArgument.Type.PHRASE)
                                }

                                usage {
                                    literalArgument("remove")
                                    stringArgument("value(s)", TextArgument.Type.PHRASE)
                                }

                                usage {
                                    literalArgument("clear")

                                    executes {

                                    }
                                }
                            }

                            override fun CommandContext.execute() {

                            }
                        })
                    }
                    else -> {
                        children(object : Command(parameter.key) {
                            init {
                                usage {
                                    when(parameter) {
                                        is IntegerElement -> integerArgument("value")
                                        is LongElement -> longArgument("value")
                                        is FloatElement -> floatArgument("value")
                                        is DoubleElement -> doubleArgument("value")
                                        is StringElement -> stringArgument("value")
                                        is BooleanElement -> booleanArgument("value")
                                    }
                                }
                            }

                            override fun CommandContext.execute() {

                            }
                        })
                    }
                }
            }
        }
    }
}