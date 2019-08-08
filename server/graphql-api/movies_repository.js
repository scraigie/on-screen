const { filter, includes } = require('lodash')
const webClient = require('./web_client.js');
const url = require('url')

const baseUrl = process.env.MOVIE_DB_ENDPOINT

function buildUrl(path) {
    let result = url.resolve(baseUrl, `/3${path}?api_key=${process.env.API_KEY}`)
    return result
}

function getMovies() {
    return webClient.concurrentGet(
        buildUrl(`/movie/popular`),
        buildUrl(`/genre/movie/list`),
    )
    .then( results => { 
        let [ { results: movies } , { genres }, configuration ] = results
        return movies.map( movie => {
            movie.genres = filter(genres, (genre) => 
                includes(movie.genre_ids, genre.id )
            )
            return movie
        })
    });
}

function getMovieDetail(id) {
    return webClient.get(buildUrl(`/movie/${id}`))
}

function getGenres() {
    return webClient.get(buildUrl(`/genre/movie/list`))
    .then(res => res.genres);
}

function getCast(id) {
    return webClient.get(buildUrl(`/movie/${id}/credits`))
    .then(res => res.cast);
}

function getCredits(id) {
    return webClient.get(buildUrl(`/movie/${id}/credits`));
}

module.exports = {
    getMovies,
    getMovieDetail,
    getGenres,
    getCast,
    getCredits
}