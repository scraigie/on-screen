package movies

import (
	"net/http"
	"bff-mobile/graphql-client"
	"github.com/go-chi/chi"
	"github.com/go-chi/render"
	"strconv"
	"fmt"
)

type Collection struct {
	Title string `json:"title"`
	Type string `json:"type"`
	Movies []Movie `json:"movies"`
}

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
			Page []Collection `json:"page"`
		} `json:"data"`
		Errors interface{} `json:"errors"`
	}

	graphql_client.Query(MoviesHomePageQuery, &resp)

	MapCollection(&resp.Data.Page, func(collection *Collection) {
		MapMovie(&collection.Movies, func(movie *Movie) {
			(*movie).Link = MoviesRoutes.DETAIL.GetLink(strconv.Itoa((*movie).Id))
		})
	})
	
	render.JSON(w, r, resp.Data.Page)
}

func MapCollection(arr *[]Collection,f func(collection *Collection)) {
    for i, _ := range *arr {
		f(&(*arr)[i])
    }
}

func MapMovie(arr *[]Movie, f func(*Movie)) {
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

	addMovieRouteLink(&resp.Data.MovieObj, movieId)

	render.JSON(w, r, resp.Data)
}