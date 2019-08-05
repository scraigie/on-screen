package uk.co.scraigie.onscreen.movies.data.repositories

import io.reactivex.Observable
import uk.co.scraigie.onscreen.movies.data.MoviesApi
import uk.co.scraigie.onscreen.movies.data.dtos.MoviesHomeDTO

interface MoviesRepository {

    fun getHomeContent() : Observable<MoviesHomeDTO>

    class Impl(val api: MoviesApi) : MoviesRepository {
        override fun getHomeContent(): Observable<MoviesHomeDTO> {
            return api.moviesHome().toObservable()
        }
    }
}