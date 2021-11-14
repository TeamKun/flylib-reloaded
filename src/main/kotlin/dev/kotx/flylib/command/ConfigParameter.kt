package dev.kotx.flylib.command

interface ConfigParameter<T> {
    val key: String
    val value: T?
}