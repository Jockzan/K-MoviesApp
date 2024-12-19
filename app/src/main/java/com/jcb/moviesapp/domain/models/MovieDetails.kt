package com.jcb.moviesapp.domain.models

data class MovieDetails(
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
    val genres: List<Genre>,
    val homepage: String?,
    val backdropPath: String?,
    val belongsToCollection: Collection?,
    val imdbId: String?,
    val originCountry: List<String>,
    val originalLanguage: String,
    val originalTitle: String,
    val posterPath: String?,
    val productionCompanies: List<ProductionCompany>,
    val productionCountries: List<ProductionCountry>,
    val releaseDate: String,
    val spokenLanguages: List<SpokenLanguage>,
    val voteAverage: Double,
    val voteCount: Int,
)

data class Collection(
    val id: Int,
    val name: String,
    val posterPath: String?,
    val backdropPath: String?,
)

data class Genre(
    val id: Int,
    val name: String,
)

data class ProductionCompany(
    val id: Int,
    val name: String,
    val logoPath: String?,
    val originCountry: String,
)

data class ProductionCountry(
    val name: String,
    val iso: String,
)

data class SpokenLanguage(
    val name: String,
    val englishName: String,
    val iso: String,
)
