/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command

import dev.kotx.flylib.command.elements.BooleanArrayElement
import dev.kotx.flylib.command.elements.BooleanElement
import dev.kotx.flylib.command.elements.DoubleArrayElement
import dev.kotx.flylib.command.elements.DoubleElement
import dev.kotx.flylib.command.elements.FloatArrayElement
import dev.kotx.flylib.command.elements.FloatElement
import dev.kotx.flylib.command.elements.IntegerArrayElement
import dev.kotx.flylib.command.elements.IntegerElement
import dev.kotx.flylib.command.elements.LongArrayElement
import dev.kotx.flylib.command.elements.LongElement
import dev.kotx.flylib.command.elements.ObjectElement
import dev.kotx.flylib.command.elements.StringArrayElement
import dev.kotx.flylib.command.elements.StringElement

class ConfigBuilder {
    private val parameters = mutableListOf<ConfigElement<*>>()

    @JvmOverloads
    fun integer(
        key: String,
        defaultValue: Int? = null,
        min: Int = Int.MIN_VALUE,
        max: Int = Int.MAX_VALUE
    ): ConfigBuilder {
        parameters.add(IntegerElement(key, defaultValue, min, max))
        return this
    }

    @JvmOverloads
    fun integerArray(
        key: String,
        defaultValue: List<Int>? = null,
    ): ConfigBuilder {
        parameters.add(IntegerArrayElement(key, defaultValue?.toMutableList()))
        return this
    }

    @JvmOverloads
    fun long(
        key: String,
        defaultValue: Long? = null,
        min: Long = Long.MIN_VALUE,
        max: Long = Long.MAX_VALUE
    ): ConfigBuilder {
        parameters.add(LongElement(key, defaultValue, min, max))
        return this
    }

    @JvmOverloads
    fun longArray(
        key: String,
        defaultValue: List<Long>? = null,
    ): ConfigBuilder {
        parameters.add(LongArrayElement(key, defaultValue?.toMutableList()))
        return this
    }

    @JvmOverloads
    fun float(
        key: String,
        defaultValue: Float? = null,
        min: Float = Float.MIN_VALUE,
        max: Float = Float.MAX_VALUE
    ): ConfigBuilder {
        parameters.add(FloatElement(key, defaultValue, min, max))
        return this
    }

    @JvmOverloads
    fun floatArray(
        key: String,
        defaultValue: List<Float>? = null,
    ): ConfigBuilder {
        parameters.add(FloatArrayElement(key, defaultValue?.toMutableList()))
        return this
    }

    @JvmOverloads
    fun double(
        key: String,
        defaultValue: Double? = null,
        min: Double = Double.MIN_VALUE,
        max: Double = Double.MAX_VALUE
    ): ConfigBuilder {
        parameters.add(DoubleElement(key, defaultValue, min, max))
        return this
    }

    @JvmOverloads
    fun doubleArray(
        key: String,
        defaultValue: List<Double>? = null,
    ): ConfigBuilder {
        parameters.add(DoubleArrayElement(key, defaultValue?.toMutableList()))
        return this
    }

    @JvmOverloads
    fun boolean(
        key: String,
        defaultValue: Boolean? = null
    ): ConfigBuilder {
        parameters.add(BooleanElement(key, defaultValue))
        return this
    }

    @JvmOverloads
    fun booleanArray(
        key: String,
        defaultValue: List<Boolean>? = null
    ): ConfigBuilder {
        parameters.add(BooleanArrayElement(key, defaultValue?.toMutableList()))
        return this
    }

    @JvmOverloads
    fun string(
        key: String,
        defaultValue: String? = null
    ): ConfigBuilder {
        parameters.add(StringElement(key, defaultValue))
        return this
    }

    @JvmOverloads
    fun stringArray(
        key: String,
        defaultValue: List<String>? = null
    ): ConfigBuilder {
        parameters.add(StringArrayElement(key, defaultValue?.toMutableList()))
        return this
    }

    @JvmOverloads
    fun obj(key: String, objectBuilder: ConfigBuilder.() -> Unit = {}): ConfigBuilder {
        parameters.add(ObjectElement(key, ConfigBuilder().apply(objectBuilder).build()))
        return this
    }

    fun build() = Config(parameters)
}