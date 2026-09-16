package org.velvetinvesting.jantanivesh.app.core.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module

expect val platformModule: Module

fun initializeKoin(
    extaModules: List<Module> = emptyList(),
    config: (KoinApplication.() -> Unit)? = null
) {
    startKoin {
        config?.invoke(this)
        modules(
            listOf(
                platformModule,
                viewModelModule,
                repositoryModule,
                useCaseModule,
            )
            + extaModules
        )
    }
}