/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
annotation class Min(
    val min: Long
)
