package movies

import (
	"net/http"

	"github.com/go-chi/chi"
	"github.com/go-chi/render"
)

type Movies struct {
	Title string `json:"title"`
	ReleaseDate string `json:"releaseDate"`
	Director string `json:"director"`
	Rating string `json:"rating"`
	ImageUrl string `json:"imageUrl"`
}

func Routes() *chi.Mux {
	router := chi.NewRouter()
	router.Get("/", getMovies)
	return router
}

func getMovies(w http.ResponseWriter, r *http.Request) {
	movies := []Movies {
		{
			Title: "",
			ReleaseDate: "",
			Director: "",
			Rating: "",
			ImageUrl: "",
		},
	}
	render.JSON(w, r, movies)
}