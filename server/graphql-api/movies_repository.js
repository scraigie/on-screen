const { filter, includes, isNil } = require('lodash')
const webClient = require('./web_client.js');
const url = require('url')

const baseUrl = process.env.MOVIE_DB_ENDPOINT
const configurationUrl = process.env.CONFIGURATION_ENDPOINT

function buildUrl(path) {
    let result = url.resolve(baseUrl, `/3${path}?api_key=${process.env.API_KEY}`)
    return result
}

function getMovies() {
    return webClient.concurrentGet(
        buildUrl(`/movie/popular`),
        buildUrl(`/genre/movie/list`),
        configurationUrl
    )
    .then( results => { 
        let [ { results: movies } , { genres }, { image_base_url } ] = results
        return movies.map( movie => {
            movie.genres = filter(genres, (genre) => 
                includes(movie.genre_ids, genre.id )
            )
            movie.poster_image_url = `${image_base_url}${movie.poster_path}`

            return movie
        })
    })
}

function getMovieDetail(id) {
    return webClient.concurrentGet(
        configurationUrl,
        buildUrl(`/movie/${id}`))
        .then( response => {
            let [ { image_base_url }, movie ] = response
            movie.poster_image_url = `${image_base_url}${movie.poster_path}`
            return movie
        })
}

function getGenres() {
    return webClient.get(buildUrl(`/genre/movie/list`))
    .then(res => res.genres);
}

function getCredits(id) {
    return webClient.concurrentGet(
        configurationUrl,
        buildUrl(`/movie/${id}/credits`))
        .then(res => { 
            let [ { image_base_url }, credits ] = res
            credits.cast.map(castItem => {
                let profileImage = castItem.profile_path
                castItem.profile_image_url = isNil(profileImage) ? null : `${image_base_url}${profileImage}`
            })
            credits.crew.map(crewItem => {
                let profileImage = crewItem.profile_path
                crewItem.profile_image_url = isNil(profileImage) ? null : `${image_base_url}${profileImage}`
            })
            return credits
        });
}

module.exports = {
    getMovies,
    getMovieDetail,
    getGenres,
    getCredits
}