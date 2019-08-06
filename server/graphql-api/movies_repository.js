const { filter, includes } = require('lodash')
const webClient = require('./web_client.js');
const url = require('url')

const baseUrl = process.env.MOVIE_DB_ENDPOINT

function buildUrl(path) {
    let result = url.resolve(baseUrl, `${path}?api_key=${process.env.API_KEY}`)
    console.log(`result = ${result}`)
    return result
}

function getMovies() {
    return webClient.concurrentGet(
        buildUrl(`/v1/api/mock/movies`),
        buildUrl(`/v1/api/mock/genres`)
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
    return webClient.get(buildUrl(`/v1/api/mock/movies/${id}`))
}

function getGenres() {
    return webClient.get(buildUrl(`/v1/api/mock/genres`))
    .then(res => res.genres);
}

function getCast(id) {
    return webClient.get(buildUrl(`/v1/api/mock/movies/${id}/cast`))
    .then(res => res.cast);
}

function getCredits(id) {
    return webClient.get(buildUrl(`/v1/api/mock/movies/${id}/cast`));
}

module.exports = {
    getMovies,
    getMovieDetail,
    getGenres,
    getCast,
    getCredits
}