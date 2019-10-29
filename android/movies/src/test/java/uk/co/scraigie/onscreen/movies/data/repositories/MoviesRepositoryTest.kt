package uk.co.scraigie.onscreen.movies.data.repositories

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import uk.co.scraigie.onscreen.movies.data.MoviesApi
import uk.co.scraigie.onscreen.movies.data.dtos.MovieDto
import uk.co.scraigie.onscreen.movies.data.dtos.MovieCollectionDTO

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class MoviesRepositoryTest {

    val moviesApiMock: MoviesApi = mock()
    lateinit var sut: MoviesRepository

    @BeforeAll
    fun setUp() {
        sut = MoviesRepository.Impl(moviesApiMock)
    }

    @BeforeEach
    fun beforeEach() {

    }

    @AfterEach
    fun afterEach() {
        moviesApiMock
    }

    @AfterAll
    fun teardown() {

    }

    @Test
    @DisplayName("Given contacts are loaded but the ID is invalid, When we find a contact, Then no contact is returned.")
    fun findContact_givenExistingId() {
        assertThat(true).isEqualTo(true)
    }

    @Nested
    @DisplayName("function getMoviesHome")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetMoviesHome  {

        @AfterEach
        fun teardown() {
            reset(moviesApiMock)
        }

        @Test
        @DisplayName("Returns result from moviesList api")
        fun returns_movies_api_result() {

            whenever(moviesApiMock.moviesHome()).thenReturn(Single.just(MovieCollectionDTO(
                movies = listOf(
                    MovieDto(
                        title = "movieTitle"
                    )
                )
            )))

            sut.getMoviesHome().test()
                .assertValueCount(1)
                .assertValue { it.movies.count() == 1 }
                .assertValue { it.movies.first().title == "movieTitle"}
        }

        @Test
        @DisplayName("Returns default params to not break missing properties during deserialisation")
        fun returns_default_params(){

            whenever(moviesApiMock.moviesHome()).thenReturn(Single.just(MovieCollectionDTO(
                movies = listOf(
                    MovieDto()
                )
            )))

            sut.getMoviesHome().test()
                .assertValueCount(1)
                .assertValue { it.movies.count() == 1 }
                .assertValue { it.movies.first().title == ""}
        }
    }
}