const { filter, take, isNil } = require('lodash');
const { makeExecutableSchema } = require('graphql-tools');

const moviesRepo = require('./movies_repository.js')
const pagesRepo = require('./pages_repository.js')

const typeDefs = `

  type Collection {
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

  type Genre {
    id: Int!
    name: String
  }

  type Person {
      name: String
      primary_department: String
      birth_date: String
      birth_place: String
      image_url: String
  }

  type Cast {
      character: String
      personId: Int!
      name: String
      profile_image_url: String
  }

  type Crew {
      job: String
      personId: Int!
      name: String
      profile_image_url: String
  }

  type Query {
    page(id: String!): [Collection]
    movies: [Movie]
    movie(id: Int!): Movie
    people: [Person]
  }
`;

const people = [
    { id: 1, name: "Leonardo DiCaprio", department: "Acting", dob: "17/04/1983", place: "London, UK", profile: "/phxiKFDvPeQj4AbkvJLmuZEieDU.jpg" },
    { id: 2, name: "Quentin Tarantino", department: "Directing", dob: "30/08/1974", place: "New York, USA", profile: "/phxiKFDvPeQj4AbkvJLmuZEieDU.jpg" }
]

const resolvers = {
  Query: {
    page: ( _, { id }) => pagesRepo.getPage(id),
    movies: () => moviesRepo.getMovies(),
    movie: ( _ ,{ id: movieId }) => moviesRepo.getMovieDetail(movieId),
    people: () => people
  },

  Movie: {
    rating: movie => movie.vote_average,
    cast: ({id: movieId}, args, ctx) => {
      ctx.credits = moviesRepo.getCredits(movieId)
      return ctx.credits.then(res => isNil(args.maxItems) ? res.cast : take(res.cast, args.maxItems));  
    },
    crew: ({}, args, ctx) => {
      return ctx.credits.then(res => isNil(args.department) ? res.crew : filter(res.crew, crewMember => crewMember.department == args.department));
    }
  }, 
  
  Cast: {
    personId: cast => cast.id,
  },

  Crew: {
    personId: crew => crew.id,
  },

  Person: {
    primary_department: person => person.department
  }
};

module.exports = makeExecutableSchema({
  typeDefs,
  resolvers,
});