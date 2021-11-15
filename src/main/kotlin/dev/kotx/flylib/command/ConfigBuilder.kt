package dev.kotx.flylib.command

import dev.kotx.flylib.command.parameters.BooleanArrayElement
import dev.kotx.flylib.command.parameters.BooleanElement
import dev.kotx.flylib.command.parameters.DoubleArrayElement
import dev.kotx.flylib.command.parameters.DoubleElement
import dev.kotx.flylib.command.parameters.FloatArrayElement
import dev.kotx.flylib.command.parameters.FloatElement
import dev.kotx.flylib.command.parameters.IntegerArrayElement
import dev.kotx.flylib.command.parameters.IntegerElement
import dev.kotx.flylib.command.parameters.LongArrayElement
import dev.kotx.flylib.command.parameters.LongElement
import dev.kotx.flylib.command.parameters.ObjectElement
import dev.kotx.flylib.command.parameters.StringArrayElement
import dev.kotx.flylib.command.parameters.StringElement

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
        defaultValue: Array<Int>? = null,
    ): ConfigBuilder {
        parameters.add(IntegerArrayElement(key, defaultValue))
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
        defaultValue: Array<Long>? = null,
    ): ConfigBuilder {
        parameters.add(LongArrayElement(key, defaultValue))
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
        defaultValue: Array<Float>? = null,
    ): ConfigBuilder {
        parameters.add(FloatArrayElement(key, defaultValue))
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
        defaultValue: Array<Double>? = null,
    ): ConfigBuilder {
        parameters.add(DoubleArrayElement(key, defaultValue))
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
        defaultValue: Array<Boolean>? = null
    ): ConfigBuilder {
        parameters.add(BooleanArrayElement(key, defaultValue))
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
        defaultValue: Array<String>? = null
    ): ConfigBuilder {
        parameters.add(StringArrayElement(key, defaultValue))
        return this
    }

    @JvmOverloads
    fun obj(key: String, objectBuilder: ConfigBuilder.() -> Unit = {}): ConfigBuilder {
        parameters.add(ObjectElement(key, ConfigBuilder().apply(objectBuilder).build()))
        return this
    }

    fun build() = Config(parameters)
}