package dev.kotx.flylib.command

import dev.kotx.flylib.command.parameters.BooleanArrayParameter
import dev.kotx.flylib.command.parameters.BooleanParameter
import dev.kotx.flylib.command.parameters.DoubleArrayParameter
import dev.kotx.flylib.command.parameters.DoubleParameter
import dev.kotx.flylib.command.parameters.FloatArrayParameter
import dev.kotx.flylib.command.parameters.FloatParameter
import dev.kotx.flylib.command.parameters.IntegerArrayParameter
import dev.kotx.flylib.command.parameters.IntegerParameter
import dev.kotx.flylib.command.parameters.LongArrayParameter
import dev.kotx.flylib.command.parameters.LongParameter
import dev.kotx.flylib.command.parameters.ObjectParameter

class ConfigBuilder {
    private val parameters = mutableListOf<ConfigParameter<*>>()

    @JvmOverloads
    fun integer(
        key: String,
        defaultValue: Int? = null,
        min: Int = Int.MIN_VALUE,
        max: Int = Int.MAX_VALUE
    ): ConfigBuilder {
        parameters.add(IntegerParameter(key, defaultValue, min, max))
        return this
    }

    @JvmOverloads
    fun integerArray(
        key: String,
        defaultValue: Array<Int>? = null,
    ): ConfigBuilder {
        parameters.add(IntegerArrayParameter(key, defaultValue))
        return this
    }

    @JvmOverloads
    fun long(
        key: String,
        defaultValue: Long? = null,
        min: Long = Long.MIN_VALUE,
        max: Long = Long.MAX_VALUE
    ): ConfigBuilder {
        parameters.add(LongParameter(key, defaultValue, min, max))
        return this
    }

    @JvmOverloads
    fun longArray(
        key: String,
        defaultValue: Array<Long>? = null,
    ): ConfigBuilder {
        parameters.add(LongArrayParameter(key, defaultValue))
        return this
    }

    @JvmOverloads
    fun float(
        key: String,
        defaultValue: Float? = null,
        min: Float = Float.MIN_VALUE,
        max: Float = Float.MAX_VALUE
    ): ConfigBuilder {
        parameters.add(FloatParameter(key, defaultValue, min, max))
        return this
    }

    @JvmOverloads
    fun floatArray(
        key: String,
        defaultValue: Array<Float>? = null,
    ): ConfigBuilder {
        parameters.add(FloatArrayParameter(key, defaultValue))
        return this
    }

    @JvmOverloads
    fun double(
        key: String,
        defaultValue: Double? = null,
        min: Double = Double.MIN_VALUE,
        max: Double = Double.MAX_VALUE
    ): ConfigBuilder {
        parameters.add(DoubleParameter(key, defaultValue, min, max))
        return this
    }

    @JvmOverloads
    fun doubleArray(
        key: String,
        defaultValue: Array<Double>? = null,
    ): ConfigBuilder {
        parameters.add(DoubleArrayParameter(key, defaultValue))
        return this
    }

    @JvmOverloads
    fun boolean(
        key: String,
        defaultValue: Boolean? = null
    ): ConfigBuilder {
        parameters.add(BooleanParameter(key, defaultValue))
        return this
    }

    @JvmOverloads
    fun booleanArray(
        key: String,
        defaultValue: Array<Boolean>? = null
    ): ConfigBuilder {
        parameters.add(BooleanArrayParameter(key, defaultValue))
        return this
    }

    @JvmOverloads
    fun obj(key: String, objectBuilder: ConfigBuilder.() -> Unit = {}): ConfigBuilder {
        parameters.add(ObjectParameter(key, ConfigBuilder().apply(objectBuilder).build()))
        return this
    }

    fun build() = Config(parameters)
}