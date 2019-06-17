package uk.co.scraigie.onscreen.di

import org.koin.dsl.module
import uk.co.scraigie.onscreen.movies.MoviesHomePresenter

val dependencyModules = listOf(
    module {
        factory { MoviesHomePresenter() }
    }
)