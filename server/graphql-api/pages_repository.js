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
        { title: "Popular", type: "CAROUSEL", movies: moviesRepo.getPopular() },
        { title: "In Cinemas", type: "CAROUSEL", movies: moviesRepo.getNowPlaying() },
        { title: "Top Rated", type: "CAROUSEL", movies: moviesRepo.getTopRated() },
        { title: "Coming Soon", type: "CAROUSEL", movies: moviesRepo.getUpcoming() },
    ]
}

module.exports = {
    getPage
}