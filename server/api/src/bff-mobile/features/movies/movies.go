package movies

import (
	"net/http"
	"bff-mobile/graphql-client"
	"github.com/go-chi/chi"
	"github.com/go-chi/render"
	"strconv"
	"fmt"
)

type Link struct {
	Href string `json:"href"`
}

type Movie struct {
	Name string `json:"title"`
	Link Link `json:"link"`
	Cast interface{} `json:"cast"`
	Crew interface{} `json:"crew"`
	Genres interface{} `json:"genres"`
	Rating float32 `json:"rating"`
	Id int `json:"id"`
}

func getMovies(w http.ResponseWriter, r *http.Request) {

	var resp struct {
		Data struct {
			Movies []Movie `json:"movies"`
		} `json:"data"`
	}

	graphql_client.Query(MoviesQuery, &resp)
	
	for i, item := range resp.Data.Movies {
		addMovieRouteLink(&resp.Data.Movies[i],strconv.Itoa(item.Id))
	}

	render.JSON(w, r, resp.Data)
}

func addMovieRouteLink(movie *Movie, movieId string) {
	detailRoute := MoviesRoutes.DETAIL.GetRoute(movieId)
	movie.Link = Link {
		Href: detailRoute,
	}
}

func getMovieDetail(w http.ResponseWriter, r *http.Request) {
	movieId := chi.URLParam(r,"id")
	
	var resp struct {
		Data struct{
			MovieObj Movie `json:"movie"`
		} `json:"data"`
	}

	query := fmt.Sprintf(MovieDetailQuery, movieId)
	graphql_client.Query(query, &resp)
	
	addMovieRouteLink(&resp.Data.MovieObj, movieId)

	render.JSON(w, r, resp.Data)
}