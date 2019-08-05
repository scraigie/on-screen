package uk.co.scraigie.onscreen.movies.domain

import io.reactivex.Observable
import uk.co.scraigie.onscreen.movies.data.dtos.MoviesHomeDTO
import uk.co.scraigie.onscreen.movies.data.repositories.MoviesRepository

interface MoviesInteractor {

    fun getHomeContent() : Observable<MoviesHomeDTO>

    class Impl(val moviesRepository: MoviesRepository) : MoviesInteractor {
        override fun getHomeContent(): Observable<MoviesHomeDTO> {
            return moviesRepository.getHomeContent()
        }
    }
}
