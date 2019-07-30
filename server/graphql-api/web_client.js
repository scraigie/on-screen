const axios = require('axios');
const LRUCache = require('lru-cache')
const { cacheAdapterEnhancer } = require('axios-extensions');
const FIVE_MINUTES = 5 * 60 * 1000

const http = axios.create({
	baseURL: '/',
	headers: { 'Cache-Control': 'no-cache' },
	adapter: cacheAdapterEnhancer(axios.defaults.adapter, { defaultCache: new LRUCache({
    maxAge: FIVE_MINUTES, 
    max: 100
  })})
});

function get(url) {
  return http.get(url).then(res => res.data );
}

function concurrentGet(...urls) {
  return axios.all(urls.map(url => get(url)))
}

module.exports = {
  get,
  concurrentGet
}