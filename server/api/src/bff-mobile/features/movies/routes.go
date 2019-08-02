package movies

import (
	"path"
    "strings"
    "github.com/go-chi/chi"
    "bff-mobile/config"
)

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
    HOME: MoviesHomeRoute(path.Join(config.V1_BASE, config.MOVIES_BASE)), 
    DETAIL: MovieDetailRoute(path.Join(config.V1_BASE, config.MOVIES_BASE, "{id}")),
}

func (r MovieDetailRoute) GetLink(movieId string) Link {
    return Link {
        Href: strings.ReplaceAll(string(r),"{id}", movieId),
        Type: MOVIE_DETAIL,
    }
}

type LinkEnum string

const (
    MOVIE_DETAIL string = "MOVIE_DETAIL"
)

type Link struct {
	Href string `json:"href"`
	Type string `json:"type"`
}
