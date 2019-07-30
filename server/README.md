# On-Screen
The aim of this project is to both experiment with new technologies, and also showcase my development skills in Android - my primary development platform.  In the real world it would be pretty strange to have all services written in different languages, but this is intentional for the sake of learning!

The backend is sitting behind an nginx reverse proxy for the sole purpose of http caching (for now).

Below nginx is a BFF (Backend for frontend) which is written in Golang. Golang is a language of interest to me and one I have had no previous experience developing with.  

Below the bff is a GraphQL layer.  I opted to not expose this directly to the frontend client(s) to abstract the queries, and also as i want to enrich the data with resource hrefs which are looked up from the bff routing table. If i was to add other platform clients (eg web) they would have their own BFF sitting on top of this application.  I have chosen to write this in NodeJS as I like the JS GraphQL API, and I also have plans to integrate MongoDB later on.


# Build & Run
#### bff
`docker build -t bff . && docker run -itd -p 8080:8080 bff`

#### nginx
`docker build -t nginx-cache . && docker run -itd -p 8081:80 --mount type=bind,source="$(pwd)"/logs,target=/var/log/nginx nginx-cache`


# Decision Log

#### Tech-debt
Created singleton with map of routes initialised in route walk. Used to resolve hrefs as `go-chi` doesn't have support for querying registered URLs (A feature of `Gorilla-Mux` for example)
https://github.com/go-chi/chi/issues/209

