package movies

const MoviesQuery = `
query {
	movies {
	  title
	  rating
		id
		poster_path
	  genres {
			name
	  }
	  cast(maxItems: 3) {
			name
			profile_image
	  }
	  crew(department: "Directing") {
			name
			profile_image
			job
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
			character
	  }
	  crew {
			name
			profile_image
			job
	  }
	}
  }
`