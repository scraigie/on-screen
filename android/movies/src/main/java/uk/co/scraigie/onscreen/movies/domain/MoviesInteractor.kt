package uk.co.scraigie.onscreen.movies.domain

import io.reactivex.Observable
import uk.co.scraigie.onscreen.movies.data.dtos.MovieCollectionDTO
import uk.co.scraigie.onscreen.movies.data.repositories.MoviesRepository

interface MoviesInteractor {

    fun getHomeContent() : Observable<List<MovieCollectionDTO>>

    class Impl(private val moviesRepository: MoviesRepository) : MoviesInteractor {
        override fun getHomeContent(): Observable<List<MovieCollectionDTO>> =
                moviesRepository.getHomeContent()
    }
}
