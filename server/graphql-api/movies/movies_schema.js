const { filter, take, isNil } = require('lodash');

const moviesRepo = require('./movies_repository.js')

const typeDefs = `

  type MovieCollection {
    title: String
    type: String
    movies: [Movie]
  }

  type Movie {
    id: Int!
    title: String
    rating: Float
    genres: [Genre]
    poster_image_url: String
    cast(maxItems: Int): [Cast]
    crew(department: String): [Crew]
  }
`;

const resolvers = {

  Movie: {
    rating: movie => movie.vote_average,
    cast: ({id: movieId}, args, ctx) => {
      ctx.credits = moviesRepo.getCredits(movieId)
      return ctx.credits.then(res => isNil(args.maxItems) ? res.cast : take(res.cast, args.maxItems));  
    },
    crew: ({}, args, ctx) => {
      return ctx.credits.then(res => isNil(args.department) ? res.crew : filter(res.crew, crewMember => crewMember.department == args.department));
    }
  }
};

module.exports = {
  typeDefs,
  resolvers,
};