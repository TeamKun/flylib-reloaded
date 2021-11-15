package dev.kotx.flylib.command

interface ConfigElement<T> {
    val key: String
    val value: T?
}