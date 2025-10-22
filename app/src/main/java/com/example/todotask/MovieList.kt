package com.example.todotask

import java.lang.Boolean.FALSE

data class Movie(
    var title: String,
    var description: String,
    var image: Int,
    var rating: Float,
    var isOnWatchlist: Boolean = FALSE
)
