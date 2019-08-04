package uk.co.scraigie.onscreen.movies.data.dtos

import com.google.gson.annotations.SerializedName

data class MoviesHomeDTO(
    val movies: List<MovieDto> = emptyList()
)

data class MovieDto(
    val title: String = "",
    val cast: List<CastDto> = emptyList(),
    val crew: List<CrewDto> = emptyList(),
    val genres: List<GenreDto> = emptyList(),
    val link: LinkDto = LinkDto(),
    val rating: Float = -1F
)

enum class LinkType {
    MOVIE_DETAIL
}

data class LinkDto(
    val type: LinkType = LinkType.MOVIE_DETAIL,
    val href: String = ""
)

data class CastDto(
    val name: String = "",
    @SerializedName("profile_image") val imagePath: String = ""
)

data class CrewDto(
    val name: String = "",
    @SerializedName("profile_image") val imagePath: String = "",
    val job: String = ""
)

data class GenreDto(
    val name: String = ""
)