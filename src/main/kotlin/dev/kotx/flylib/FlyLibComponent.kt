/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib

import org.koin.core.*
import org.koin.core.component.*

class FlyLibComponent: KoinComponent {
    override fun getKoin(): Koin = FlyLibContext.get()
}