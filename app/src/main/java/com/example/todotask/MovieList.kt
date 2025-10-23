package com.example.todotask

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlin.random.Random

data class Movie(
    val title: String,
    val desc: String,
    val rating: Int,
    var isOnWatchList: Boolean,
    var image: Int,
    val movieID: Int
)

class MovieViewModel : ViewModel() {
    val movieList = mutableStateListOf<Movie>()      // master list of all movies

    init {          // run this once to fill up the movie list
        populateMovieList()
    }

    fun populateMovieList() {
        for (index in 0 until 10){
            val image = if (index < images.size) images[index] else null // handling for testing, image list is currently empty
            movieList.add(Movie(
                title = if(index < images.size) titles[index] else "Movie #${index+1}",
                desc = if(index < images.size) descs[index] else "Description for Movie #${index+1}",
                rating = if(index < images.size) ratings[index] else Random.nextInt(0, 5),
                isOnWatchList = false,
                image = if(index < images.size) images[index] else R.drawable.movie_placeholder,
                movieID = index
            ))
        }

    }

    fun updateMovie(movie : Movie, index : Int, isChecked : Boolean) {  // takes a movie on and off the watchlist based on "isChecked"
        movieList[index] = movie.copy(isOnWatchList = isChecked )
    }
}

// ordered list of data used when constructing the master list of Movie objects
val titles       = listOf<String>("Captain Marvel", "Avengers: Endgame")
val descs       = listOf<String>("\nCarol Danvers becomes one of the universe's most powerful heroes when Earth is caught in the middle of a galactic war between two alien races.",
    "\nAfter the devastating events of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe.")
val ratings     = listOf<Int>(4,5)
val images      = listOf<Int>(R.drawable.captain_marvel, R.drawable.avengers_endgame)
