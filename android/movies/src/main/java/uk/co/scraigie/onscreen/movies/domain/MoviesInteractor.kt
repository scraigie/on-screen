package uk.co.scraigie.onscreen.movies.domain

import io.reactivex.Observable
import uk.co.scraigie.onscreen.core.framework.contracts.configuration.IConfigurationInteractor
import uk.co.scraigie.onscreen.movies.data.dtos.MoviesHomeDTO
import uk.co.scraigie.onscreen.movies.data.repositories.MoviesRepository

interface MoviesInteractor {

    fun getHomeContent() : Observable<MoviesHomeDTO>

    class Impl(val moviesRepository: MoviesRepository, val configurationInteractor: IConfigurationInteractor) : MoviesInteractor {
        override fun getHomeContent(): Observable<MoviesHomeDTO> =

                moviesRepository.getHomeContent()
                    .flatMapSingle { moviesContent ->
                        configurationInteractor.baseImageUrlSingle
                            .map { moviesContent.copy(
                                
                            ) }
                    }

    }
}
