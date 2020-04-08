const { merge } = require('lodash');
const { makeExecutableSchema } = require('graphql-tools');

const commonSchema = require('./common/common_schema.js')
const moviesSchema = require('./movies/movies_schema.js')
const moviesRepo = require('./movies/movies_repository.js')
const pagesRepo = require('./pages_repository.js')

const typeDefs = `

  ${moviesSchema.typeDefs}

  ${commonSchema.typeDefs}
  
  type Query {
    page(id: String!): [MovieCollection]
    movies: [Movie]
    movie(id: Int!): Movie
    people: [Person]
  }
`;

console.log(typeDefs)

const people = [
    { id: 1, name: "Leonardo DiCaprio", department: "Acting", dob: "17/04/1983", place: "London, UK", profile: "/phxiKFDvPeQj4AbkvJLmuZEieDU.jpg" },
    { id: 2, name: "Quentin Tarantino", department: "Directing", dob: "30/08/1974", place: "New York, USA", profile: "/phxiKFDvPeQj4AbkvJLmuZEieDU.jpg" }
]

const queryResolver = {
  Query: {
    page: ( _, { id }) => pagesRepo.getPage(id),
    movie: ( _ ,{ id: movieId }) => moviesRepo.getMovieDetail(movieId),
    people: () => people
  }
}

var resolvers = merge(commonSchema.resolvers, moviesSchema.resolvers, queryResolver)

console.log(resolvers)

module.exports = makeExecutableSchema({
  typeDefs,
  resolvers,
});