/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command.elements

import dev.kotx.flylib.command.ConfigElement

class LongElement(override val key: String, override var value: Long?, val min: Long, val max: Long) :
    ConfigElement<Long>