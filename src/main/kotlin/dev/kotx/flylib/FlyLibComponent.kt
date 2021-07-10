/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib

import org.koin.core.Koin
import org.koin.core.component.KoinComponent

interface FlyLibComponent : KoinComponent {
    override fun getKoin(): Koin = FlyLibContext.get()
}