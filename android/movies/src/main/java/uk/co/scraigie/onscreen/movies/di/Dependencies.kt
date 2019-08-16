package uk.co.scraigie.onscreen.movies.di

import org.koin.dsl.module
import uk.co.scraigie.onscreen.core.data.IApiFactory
import uk.co.scraigie.onscreen.movies.data.MoviesApi
import uk.co.scraigie.onscreen.movies.data.repositories.MoviesRepository
import uk.co.scraigie.onscreen.movies.domain.MoviesInteractor
import uk.co.scraigie.onscreen.movies.ui.home.MoviesHomePresenter

val moviesModule = module {
    single { get<IApiFactory>().create(MoviesApi::class) }
    factory { MoviesHomePresenter(get()) }
    single<MoviesInteractor> { MoviesInteractor.Impl(get()) }
    single<MoviesRepository> { MoviesRepository.Impl(get()) }
}