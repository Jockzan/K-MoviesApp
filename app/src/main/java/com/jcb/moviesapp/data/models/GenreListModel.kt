package com.jcb.moviesapp.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenreListModel(
    val genres: List<GenreModel>
)