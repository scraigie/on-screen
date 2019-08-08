package mockdata

import (
	"net/http"
	"io/ioutil"
	"github.com/go-chi/chi"
	"github.com/go-chi/render"
)

func MovieRoutes() *chi.Mux {
	router := chi.NewRouter()
	router.Get("/popular", getMovies)
	router.Get("/{id}", getMovieDetail)
	router.Get("/{id}/credits", getMovieCast)
	return router
}

func GenreRoutes() *chi.Mux {
	router := chi.NewRouter()
	router.Get("/movie/list", getMovieGenres)
	return router
}

func getMovies(w http.ResponseWriter, r *http.Request) {
	b,_ := ioutil.ReadFile("features/mockdata/movies.json");
	render.PlainText(w,r,string(b))
}

func getMovieGenres(w http.ResponseWriter, r *http.Request) {
	b,_ := ioutil.ReadFile("features/mockdata/movie_genres.json");
	render.PlainText(w,r,string(b))
}

func getMovieDetail(w http.ResponseWriter, r *http.Request) {
	movieId := chi.URLParam(r,"id")
	if(movieId != "287947") {
		render.Render(w, r, ErrNotFound)
		return
	}
	b,_ := ioutil.ReadFile("features/mockdata/movie_detail.json");
	render.PlainText(w,r,string(b))
}

func getMovieCast(w http.ResponseWriter, r *http.Request) {
	movieId := chi.URLParam(r,"id")
	if(movieId != "287947") {
		render.Render(w, r, ErrNotFound)
		return
	}
	b,_ := ioutil.ReadFile("features/mockdata/cast.json");
	render.PlainText(w,r,string(b))
}

func getPersonCredits(w http.ResponseWriter, r *http.Request) {
	personId := chi.URLParam(r,"id")
	if(personId != "69899") {
		render.Render(w, r, ErrNotFound)
		return
	}
	b,_ := ioutil.ReadFile("features/mockdata/person_credits.json");
	render.PlainText(w,r,string(b))
}

func getPerson(w http.ResponseWriter, r *http.Request) {
	personId := chi.URLParam(r,"id")
	if(personId != "69899") {
		render.Render(w, r, ErrNotFound)
		return
	}
	b,_ := ioutil.ReadFile("features/mockdata/person.json");
	render.PlainText(w,r,string(b))
}

type ErrResponse struct {
	Err            error `json:"-"` // low-level runtime error
	HTTPStatusCode int   `json:"-"` // http response status code

	StatusText string `json:"status"`          // user-level status message
	AppCode    int64  `json:"code,omitempty"`  // application-specific error code
	ErrorText  string `json:"error,omitempty"` // application-level error message, for debugging
}

func (e *ErrResponse) Render(w http.ResponseWriter, r *http.Request) error {
	render.Status(r, e.HTTPStatusCode)
	return nil
}

var ErrNotFound = &ErrResponse{HTTPStatusCode: 404, StatusText: "Resource not found."}
