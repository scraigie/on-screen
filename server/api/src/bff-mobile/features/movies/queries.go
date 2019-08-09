package movies

const MoviesQuery = `
query {
	movies {
	  title
	  rating
		id
		poster_image_url
	  genres {
			name
	  }
	  cast(maxItems: 3) {
			name
			profile_image_url
	  }
	  crew(department: "Directing") {
			name
			profile_image_url
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
		poster_image_url
	  genres {
			name
	  }
	  cast {
			name
			profile_image_url
			character
	  }
	  crew {
			name
			profile_image_url
			job
	  }
	}
  }
`