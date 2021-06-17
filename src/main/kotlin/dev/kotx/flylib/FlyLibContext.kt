/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib

import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.KoinContext
import org.koin.core.error.KoinAppAlreadyStartedException

object FlyLibContext : KoinContext {
    private var koin: Koin? = null

    override fun get(): Koin = koin ?: error("KoinApplication has not been started")

    override fun getOrNull(): Koin? = koin

    override fun register(koinApplication: KoinApplication) {
        if (koin != null) {
            throw KoinAppAlreadyStartedException("A Koin Application has already been started")
        }
        koin = koinApplication.koin
    }

    override fun stop() = synchronized(this) {
        koin?.close()
        koin = null
    }

    /**
     * Start a Koin Application as StandAlone
     */
    internal fun startKoin(koinContext: KoinContext = FlyLibContext, koinApplication: KoinApplication): KoinApplication = synchronized(this) {
        koinContext.register(koinApplication)
        koinApplication.createEagerInstances()
        return koinApplication
    }
}