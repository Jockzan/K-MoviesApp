package com.jcb.moviesapp.domain.models

data class MovieList(
    val dates: Dates?,
    val page: Int,
    val results: List<Movie>,
    val totalPages: Int,
    val totalResults: Int,
)

data class Dates(
    val maximum: String,
    val minimum: String,
)

data class Movie(
    val id: Int,
    val overview: String,
    val popularity: Double,
    val title: String,
    val video: Boolean,
    val adult: Boolean,
    val backdropPath: String?,
    val genreIds: List<Int>,
    val originalLanguage: String,
    val originalTitle: String,
    val posterPath: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
)
