package graphql_client

import (
	"net/http"
	"io/ioutil"
	"bytes"
	"encoding/json"
	"log"
)

func Query(query string, obj interface{}) {//interface{} {
	requestBody, err := json.Marshal(map[string]string{
		"query": query,
	})

	if err != nil {
		log.Fatalln(err)
	}

	resp, err := http.Post("http://localhost:4000/graphql", "application/json", bytes.NewBuffer(requestBody))
	
	if err != nil {
		log.Fatalln(err)
	}

	defer resp.Body.Close()

	body, err := ioutil.ReadAll(resp.Body)

	if err != nil {
		log.Fatalln(err)
	}

	errs := json.Unmarshal(body, &obj)

	if errs != nil {
		log.Fatalln(err)
	}
}

