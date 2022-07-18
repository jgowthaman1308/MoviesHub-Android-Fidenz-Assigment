package com.example.movieshub.main.models

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class FavouritesModel(
    @Id var id: Long = 0,
    var movieOrTvID: Int = 0,
    var isMovie: Boolean = true
)