package uk.co.scraigie.onscreen.movies.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import uk.co.scraigie.onscreen.movies.data.dtos.MovieDto
import uk.co.scraigie.onscreen.movies.data.dtos.MoviesHomeDTO

interface MoviesApi {
    @GET("v1/api/movies")
    fun moviesHome(): Single<MoviesHomeDTO>

    @GET("{path}")
    fun movieDetail(@Path("path", encoded = true) path: String): Single<MovieDto>
}

