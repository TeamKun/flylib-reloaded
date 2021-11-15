/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command

interface ConfigElement<T> {
    val key: String
    val value: T?
}