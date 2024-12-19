package com.jcb.moviesapp.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDetailsModel(
    val id: Int,
    val overview: String,
    val popularity: Double,
    val revenue: Long,
    val runtime: Int,
    val status: String,
    val tagline: String?,
    val title: String,
    val video: Boolean,
    val adult: Boolean,
    val budget: Int,
    val genres: List<GenreModel>,
    val homepage: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "belongs_to_collection") val belongsToCollection: CollectionModel?,
    @Json(name = "imdb_id") val imdbId: String?,
    @Json(name = "origin_country") val originCountry: List<String>,
    @Json(name = "original_language") val originalLanguage: String,
    @Json(name = "original_title") val originalTitle: String,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "production_companies") val productionCompanies: List<ProductionCompanyModel>,
    @Json(name = "production_countries") val productionCountries: List<ProductionCountryModel>,
    @Json(name = "release_date") val releaseDate: String,
    @Json(name = "spoken_languages") val spokenLanguages: List<SpokenLanguageModel>,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "vote_count") val voteCount: Int,
)

@JsonClass(generateAdapter = true)
data class CollectionModel(
    val id: Int,
    val name: String,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
)

@JsonClass(generateAdapter = true)
data class GenreModel(
    val id: Int,
    val name: String,
)

@JsonClass(generateAdapter = true)
data class ProductionCompanyModel(
    val id: Int,
    val name: String,
    @Json(name = "logo_path") val logoPath: String?,
    @Json(name = "origin_country") val originCountry: String,
)

@JsonClass(generateAdapter = true)
data class ProductionCountryModel(
    val name: String,
    @Json(name = "iso_3166_1") val iso: String,
)

@JsonClass(generateAdapter = true)
data class SpokenLanguageModel(
    val name: String,
    @Json(name = "english_name") val englishName: String,
    @Json(name = "iso_639_1") val iso: String,
)
