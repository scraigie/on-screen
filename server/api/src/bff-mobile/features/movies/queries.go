package movies

const MoviesQuery = `
query {
	movies {
	  title
	  rating
	  id
	  genres {
		name
	  }
	  cast {
		name
		profile_image
	  }
	  crew(department: "Directing") {
		name
		profile_image
	  }
	}
  }
`

const MovieDetailQuery = `
query {
	movie(id: %s) {
	  title
	  rating
	  id
	  genres {
		name
	  }
	  cast {
		name
		profile_image
	  }
	  crew(department: "Directing") {
		name
		profile_image
	  }
	}
  }
`