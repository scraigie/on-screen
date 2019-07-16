## bff
`docker build -t bff . && docker run -itd -p 8080:8080 bff`

## build and run Nginx image
`docker build -t nginx-cache . && docker run -itd -p 8081:80 --mount type=bind,source="$(pwd)"/logs,target=/var/log/nginx nginx-cache`
