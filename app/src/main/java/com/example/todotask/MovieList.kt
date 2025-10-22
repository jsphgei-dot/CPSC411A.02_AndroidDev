package com.example.todotask

import android.media.Image
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class Movie(val title: String, val desc: String, val rating : Int, var isOnWatchList: Boolean, val image: Image?, val movieID : Int)

class MovieViewModel : ViewModel() {
    val movieList = mutableStateListOf<Movie>()      // master list of all movies

    init {          // run this once to fill up the movie list
        populateMovieList()
    }

    fun populateMovieList() {
        for (index in titles.indices){
            val image = if (index < images.size) images[index] else null // handling for testing, image list is currently empty
            movieList.add(Movie(
                title           = titles[index],
                desc            = descs[index],
                rating          = ratings[index],
                isOnWatchList   = false,
                image           = image,
                movieID         = index
            ))
        }

    }

    fun updateMovie(movie : Movie, index : Int, isChecked : Boolean) {  // takes a movie on and off the watchlist based on "isChecked"
        movieList[index] = movie.copy(isOnWatchList = isChecked )
    }
}

// ordered list of data used when constructing the master list of Movie objects
val titles       = listOf<String>("Movie1", "Movie2")
val descs       = listOf<String>("A funny movie", "A very scary movie")
val ratings     = listOf<Int>(4,5)
val images      = listOf<Image>()
