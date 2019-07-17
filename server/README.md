# Build & Run
#### bff
`docker build -t bff . && docker run -itd -p 8080:8080 bff`

#### nginx
`docker build -t nginx-cache . && docker run -itd -p 8081:80 --mount type=bind,source="$(pwd)"/logs,target=/var/log/nginx nginx-cache`


# Decision Log

#### Tech-debt
Created singleton with map of routes initialised in route walk. Used to resolve hrefs as `go-chi` doesn't have support for querying registered URLs (A feature of `Gorilla-Mux` for example)
https://github.com/go-chi/chi/issues/209


