package uk.co.scraigie.onscreen.movies.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import uk.co.scraigie.onscreen.movies.data.dtos.MovieCollectionDTO
import uk.co.scraigie.onscreen.movies.data.dtos.MovieDto

interface MoviesApi {
    @GET("v1/api/movies")
    fun moviesHome(): Single<List<MovieCollectionDTO>>

    @GET("{path}")
    fun movieDetail(@Path("path", encoded = true) path: String): Single<MovieDto>
}

