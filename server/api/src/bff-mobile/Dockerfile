FROM golang:1.14.1-alpine3.11

WORKDIR /go/src/bff-mobile
COPY . .

RUN apk add git
RUN go get -d -v ./...
RUN go install -v ./...

CMD ["bff-mobile"]