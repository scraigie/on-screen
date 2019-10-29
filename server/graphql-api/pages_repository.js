const moviesRepo = require('./movies_repository.js');

function getPage(id) {
    switch(id) {
        case "MOVIES_HOME": 
            return getMoviesPage()
    }
    return []
}

function getMoviesPage(){
    return [
        { title: "In Cinemas", type: "CAROUSEL", movies: moviesRepo.getNowPlaying() },
        { title: "Coming Soon", type: "CAROUSEL", movies: moviesRepo.getUpcoming() },
        { title: "Trending", type: "CAROUSEL", movies: moviesRepo.getPopular() },
        { title: "Top Rated", type: "CAROUSEL", movies: moviesRepo.getTopRated() },
    ]
}

module.exports = {
    getPage
}