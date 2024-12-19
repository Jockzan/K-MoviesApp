package com.jcb.moviesapp.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieListModel(
    val dates: DatesModel?,
    val page: Int,
    val results: List<MovieModel>,
    @Json(name = "total_pages") val totalPages: Int,
    @Json(name = "total_results") val totalResults: Int,
)

@JsonClass(generateAdapter = true)
data class DatesModel(
    val maximum: String,
    val minimum: String,
)

@JsonClass(generateAdapter = true)
data class MovieModel(
    val id: Int,
    val overview: String,
    val popularity: Double,
    val title: String,
    val video: Boolean,
    val adult: Boolean,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "genre_ids") val genreIds: List<Int>,
    @Json(name = "original_language") val originalLanguage: String,
    @Json(name = "original_title") val originalTitle: String,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "release_date") val releaseDate: String,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "vote_count") val voteCount: Int,
)
