package uk.co.scraigie.onscreen.di

import org.koin.dsl.module
import uk.co.scraigie.onscreen.BuildConfig
import uk.co.scraigie.onscreen.core.framework.contracts.data.IApiFactory
import uk.co.scraigie.onscreen.data.ApiFactory
import uk.co.scraigie.onscreen.movies.di.moviesModule

private val sharedModule = module {
    single<IApiFactory> { ApiFactory(get(), BuildConfig.BFF_API) }
}

private val configurationModule = module {
//    single { get<IApiFactory>().create(ConfigurationApi::class) }
//    single<IConfigurationInteractor> { ConfigurationInteractor(get()) }
}

val dependencyModules = listOf(
    sharedModule,
    moviesModule,
    configurationModule
)

