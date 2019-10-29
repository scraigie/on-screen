package uk.co.scraigie.onscreen.movies.data.repositories

import io.reactivex.Observable
import uk.co.scraigie.onscreen.movies.data.MoviesApi
import uk.co.scraigie.onscreen.movies.data.dtos.MovieCollectionDTO

interface MoviesRepository {

    fun getHomeContent() : Observable<List<MovieCollectionDTO>>

    class Impl(val api: MoviesApi) : MoviesRepository {
        override fun getHomeContent(): Observable<List<MovieCollectionDTO>> {
            return api.moviesHome().toObservable()
        }
    }
}