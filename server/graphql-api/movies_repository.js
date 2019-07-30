const { filter, includes } = require('lodash')
const webClient = require('./web_client.js');

function getMovies() {
    return webClient.concurrentGet(
        'http://localhost:8083/v1/api/mock/movies',
        'http://localhost:8083/v1/api/mock/genres'
    )
    .then( results => { 
        let [ { results: movies } , { genres } ] = results
        return movies.map( movie => {
            movie.genres = filter(genres, (genre) => 
                includes(movie.genre_ids, genre.id )
            )
            return movie
        })
    });
}

function getMovieDetail(id) {
    return webClient.get('http://localhost:8083/v1/api/mock/movies/' + id)
}

function getGenres() {
    return webClient.get('http://localhost:8083/v1/api/mock/genres')
    .then(res => res.genres);
}

function getCast(id) {
    return webClient.get('http://localhost:8083/v1/api/mock/movies/' + id + '/cast')
    .then(res => res.cast);
}

function getCredits(id) {
    return webClient.get('http://localhost:8083/v1/api/mock/movies/' + id + '/cast');
}

module.exports = {
    getMovies,
    getMovieDetail,
    getGenres,
    getCast,
    getCredits
}