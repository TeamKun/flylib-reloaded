/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command

import dev.kotx.flylib.command.elements.ArrayElement
import dev.kotx.flylib.command.elements.BooleanElement
import dev.kotx.flylib.command.elements.DoubleElement
import dev.kotx.flylib.command.elements.FloatElement
import dev.kotx.flylib.command.elements.IntegerElement
import dev.kotx.flylib.command.elements.LongElement
import dev.kotx.flylib.command.elements.ObjectElement
import dev.kotx.flylib.command.elements.StringElement

class Config(
    internal val elements: List<ConfigElement<*>>,
) {
    fun get(key: String) = getOrNull(key)!!
    fun getOrNull(key: String) = elements.find { it.key == key }
    fun contains(key: String) = elements.any { it.key == key }

    fun set(key: String, value: Int) {
        (getOrNull(key) as? IntegerElement)?.value = value
    }

    fun set(key: String, value: Long) {
        (getOrNull(key) as? LongElement)?.value = value
    }

    fun set(key: String, value: Float) {
        (getOrNull(key) as? FloatElement)?.value = value
    }

    fun set(key: String, value: Double) {
        (getOrNull(key) as? DoubleElement)?.value = value
    }

    fun set(key: String, value: String) {
        (getOrNull(key) as? StringElement)?.value = value
    }

    fun set(key: String, value: Boolean) {
        (getOrNull(key) as? BooleanElement)?.value = value
    }

    fun <T> set(key: String, value: List<T>) {
        (getOrNull(key) as? ArrayElement<T>)?.value = value.toMutableList()
    }

    fun set(key: String, block: ConfigBuilder.() -> Unit) {
        (getOrNull(key) as? ObjectElement)?.value = ConfigBuilder().apply(block).build()
    }

    fun <T> add(key: String, value: T) {
        (getOrNull(key) as? ArrayElement<T>)?.value?.add(value)
    }

    fun <T> remove(key: String, value: T) {
        (getOrNull(key) as? ArrayElement<T>)?.value?.remove(value)
    }

    fun <T> clear(key: String) {
        (getOrNull(key) as? ArrayElement<T>)?.value?.clear()
    }

    fun getInteger(key: String) = elements.find { it.key == key }!!.value as Int
    fun getLong(key: String) = elements.find { it.key == key }!!.value as Long
    fun getFloat(key: String) = elements.find { it.key == key }!!.value as Float
    fun getDouble(key: String) = elements.find { it.key == key }!!.value as Double
    fun getString(key: String) = elements.find { it.key == key }!!.value as String
    fun getBoolean(key: String) = elements.find { it.key == key }!!.value as Boolean
    fun getObject(key: String) = elements.find { it.key == key }!!.value as Config

    fun getIntegerArray(key: String) = (elements.find { it.key == key }!!.value as MutableList<Int>).toList()
    fun getLongArray(key: String) = (elements.find { it.key == key }!!.value as MutableList<Long>).toList()
    fun getFloatArray(key: String) = (elements.find { it.key == key }!!.value as MutableList<Float>).toList()
    fun getDoubleArray(key: String) = (elements.find { it.key == key }!!.value as MutableList<Double>).toList()
    fun getStringArray(key: String) = (elements.find { it.key == key }!!.value as MutableList<String>).toList()
    fun getBooleanArray(key: String) = (elements.find { it.key == key }!!.value as MutableList<Boolean>).toList()

    fun getIntegerOrNull(key: String) = elements.find { it.key == key }?.value as? Int
    fun getLongOrNull(key: String) = elements.find { it.key == key }?.value as? Long
    fun getFloatOrNull(key: String) = elements.find { it.key == key }?.value as? Float
    fun getDoubleOrNull(key: String) = elements.find { it.key == key }?.value as? Double
    fun getStringOrNull(key: String) = elements.find { it.key == key }?.value as? String
    fun getBooleanOrNull(key: String) = elements.find { it.key == key }?.value as? Boolean
    fun getObjectOrNull(key: String) = elements.find { it.key == key }?.value as? Config

    fun getIntegerArrayOrNull(key: String) = (elements.find { it.key == key }?.value as? MutableList<Int?>)?.toList()
    fun getLongArrayOrNull(key: String) = (elements.find { it.key == key }?.value as? MutableList<Long?>)?.toList()
    fun getFloatArrayOrNull(key: String) = (elements.find { it.key == key }?.value as? MutableList<Float?>)?.toList()
    fun getDoubleArrayOrNull(key: String) = (elements.find { it.key == key }?.value as? MutableList<Double?>)?.toList()
    fun getStringArrayOrNull(key: String) = (elements.find { it.key == key }?.value as? MutableList<String?>)?.toList()
    fun getBooleanArrayOrNull(key: String) =
        (elements.find { it.key == key }?.value as? MutableList<Boolean?>)?.toList()
}