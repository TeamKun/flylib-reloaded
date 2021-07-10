/*
 * Copyright (c) 2021 kotx__.
 * Twitter: https://twitter.com/kotx__
 */

package dev.kotx.flylib

import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.KoinContext
import org.koin.core.error.KoinAppAlreadyStartedException
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.koinApplication

object FlyLibContext : KoinContext {
    private var koin: Koin? = null

    override fun get(): Koin = koin ?: error("KoinApplication has not been started")
    override fun getOrNull(): Koin? = koin

    override fun register(koinApplication: KoinApplication) {
        if (koin != null)
            throw KoinAppAlreadyStartedException("A koin application has already been started")

        koin = koinApplication.koin
    }

    override fun stop() {
        koin?.close()
        koin = null
    }

    internal fun startKoin(appDeclaration: KoinAppDeclaration): KoinApplication = synchronized(this) {
        val koinApplication = koinApplication(appDeclaration)
        register(koinApplication)
        koinApplication.createEagerInstances()
    }
}