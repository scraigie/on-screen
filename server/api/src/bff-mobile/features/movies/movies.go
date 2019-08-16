package movies

import (
	"net/http"
	"bff-mobile/graphql-client"
	"github.com/go-chi/chi"
	"github.com/go-chi/render"
	"strconv"
	"fmt"
)

type Movie struct {
	Name string `json:"title"`
	Link Link `json:"link"`
	Cast interface{} `json:"cast"`
	Crew interface{} `json:"crew"`
	Genres interface{} `json:"genres"`
	Rating float32 `json:"rating"`
	Id int `json:"id"`
	PosterUrl string `json:"poster_image_url"`
}

func getMovies(w http.ResponseWriter, r *http.Request) {

	var resp struct {
		Data struct {
			Movies []Movie `json:"movies"`
		} `json:"data"`
		Errors interface{} `json:"errors"`
	}

	graphql_client.Query(MoviesQuery, &resp)
	
	// if(&resp.Errors != nil) {
	// 	http.Error(w, http.StatusText(500), 500)
	// 	return
	// }

	Map(&resp.Data.Movies, func(movie *Movie) {
		(*movie).Link = MoviesRoutes.DETAIL.GetLink(strconv.Itoa((*movie).Id))
	})

	render.JSON(w, r, resp.Data)
}

func Map(arr *[]Movie, f func(*Movie)) {
    for i, _ := range *arr {
		f(&(*arr)[i])
    }
}

func addMovieRouteLink(movie *Movie, movieId string) {
	movie.Link = MoviesRoutes.DETAIL.GetLink(movieId)
}

func getMovieDetail(w http.ResponseWriter, r *http.Request) {
	movieId := chi.URLParam(r,"id")
	
	var resp struct {
		Data struct{
			MovieObj Movie `json:"movie"`
		} `json:"data"`
		Errors interface{} `json:"errors"`
	}

	query := fmt.Sprintf(MovieDetailQuery, movieId)
	graphql_client.Query(query, &resp)
	
	// if(&resp.Errors != nil) {
	// 	http.Error(w, http.StatusText(500), 500)
	// 	return
	// }

	addMovieRouteLink(&resp.Data.MovieObj, movieId)

	render.JSON(w, r, resp.Data)
}