package movies

import (
	"path"
    "strings"
	"github.com/go-chi/chi"
)

const V1_BASE = "/v1/api"
const MOVIES_BASE = "/movies"

func Routes() *chi.Mux {
	router := chi.NewRouter()
	router.Get("/", getMovies)
	router.Get("/{id}", getMovieDetail)
	return router
}

type MoviesHomeRoute string

type MovieDetailRoute string

type IMovieDetailRoute interface {
    GetMovieDetailRoute(movieId string) string
}

var MoviesRoutes = struct {
    HOME MoviesHomeRoute
    DETAIL MovieDetailRoute 
} {
    HOME: MoviesHomeRoute(path.Join(V1_BASE, MOVIES_BASE)), 
    DETAIL: MovieDetailRoute(path.Join(V1_BASE, MOVIES_BASE, "{id}")),
}

func (r MovieDetailRoute) GetRoute(movieId string) string {
    return strings.ReplaceAll(string(r),"{id}", movieId)
}
