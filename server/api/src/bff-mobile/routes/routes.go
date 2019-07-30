package routes

import (
    "net/http"
    "sync"
    "reflect"
    "runtime"
    "strings"
    "fmt"
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

func Get(key string, params map[string]string) string {
    url := getInstance()[key]

    for k,v := range params {
       url = strings.ReplaceAll(url, fmt.Sprintf("{%s}",k), v)
    }
    
    return url
}

func getFunctionName(i interface{}) string {
	return runtime.FuncForPC(reflect.ValueOf(i).Pointer()).Name()
}
