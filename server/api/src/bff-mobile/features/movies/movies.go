package movies

import (
	"net/http"
	"io/ioutil"
	
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

    b,_ := ioutil.ReadFile("movies.json");

	render.PlainText(w,r,string(b))
	// render.JSON(w, r, movies)
}