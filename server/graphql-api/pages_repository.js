const moviesRepo = require('./movies/movies_repository.js');

function getPage(id) {
    switch(id) {
        case "MOVIES_HOME": 
            return getMoviesPage()
    }
    return []
}

function getMoviesPage(){
    return [
        { title: "The Hype Machine", type: "HERO", movies: moviesRepo.getUpcoming().then(movies => movies.slice(0,1)) },
        { title: "In Cinemas", type: "CAROUSEL", movies: moviesRepo.getNowPlaying().then(movies => movies.slice(1)) },
        { title: "Must See", type: "SINGLE", movies: moviesRepo.getNowPlaying().then(movies => movies.slice(0,1)) },
        { title: "Coming Soon", type: "CAROUSEL", movies: moviesRepo.getUpcoming().then(movies => movies.slice(1)) },
        { title: "Trending", type: "CAROUSEL", movies: moviesRepo.getPopular() },
        { title: "Top Rated", type: "CAROUSEL", movies: moviesRepo.getTopRated() },
    ]
}

module.exports = {
    getPage
}