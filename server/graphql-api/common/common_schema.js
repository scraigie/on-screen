const typeDefs = `

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
`;

const resolvers = {
  
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

module.exports = {
  typeDefs,
  resolvers,
};