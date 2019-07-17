package routes

import (
    "net/http"
    "sync"
    "reflect"
    "runtime"
)

type routeMap = map[string]string

var instance routeMap
var once sync.Once

func getInstance() routeMap {
    once.Do(func() {
        instance = make(routeMap)
    })
    return instance
}

func Add(route string, handler http.Handler) {
    funcName := getFunctionName(handler)
    getInstance()[funcName] = route
}

func Get(key string) string {
    return getInstance()[key]
}

func getFunctionName(i interface{}) string {
	return runtime.FuncForPC(reflect.ValueOf(i).Pointer()).Name()
}
