package graphql_client

import (
	"net/http"
	"io/ioutil"
	"bytes"
	"encoding/json"
	"log"
	"os"
	"fmt"
)

func Query(query string, obj interface{}) {//interface{} {
	requestBody, err := json.Marshal(map[string]string{
		"query": query,
	})

	if err != nil {
		log.Fatalln(err)
	}

	resp, err := http.Post(fmt.Sprintf("%s%s", os.Getenv("GRAPHQL_ENDPOINT"),"/graphql"), "application/json", bytes.NewBuffer(requestBody))
	
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

