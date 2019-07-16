FROM golang:1.12.7-alpine3.10

WORKDIR /go/src/bff-mobile
COPY . .

RUN go get -d -v ./...
RUN go install -v ./...

CMD ["bff-mobile"]