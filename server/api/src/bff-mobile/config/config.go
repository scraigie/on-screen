package config

import (
	"net/http"
	"io/ioutil"
	"github.com/go-chi/chi"
	"github.com/go-chi/render"
)

const V1_BASE = "/v1/api"
const MOVIES_BASE = "/movies"

func Routes() *chi.Mux {
	router := chi.NewRouter()
	router.Get("/", getConfig)
	return router
}

func getConfig(w http.ResponseWriter, r *http.Request) {

	b,_ := ioutil.ReadFile("config/configuration.json");

	render.PlainText(w,r,string(b))
}
