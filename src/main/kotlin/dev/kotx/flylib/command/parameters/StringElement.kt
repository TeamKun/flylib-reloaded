/*
 * Copyright (c) 2021 kotx__
 */

package dev.kotx.flylib.command.parameters

import dev.kotx.flylib.command.ConfigElement

class StringElement(override val key: String, override val value: String?) : ConfigElement<String>