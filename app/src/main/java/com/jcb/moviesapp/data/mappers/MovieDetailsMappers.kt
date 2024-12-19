package com.jcb.moviesapp.data.mappers

import com.jcb.moviesapp.data.models.CollectionModel
import com.jcb.moviesapp.data.models.GenreModel
import com.jcb.moviesapp.data.models.MovieDetailsModel
import com.jcb.moviesapp.data.models.ProductionCompanyModel
import com.jcb.moviesapp.data.models.ProductionCountryModel
import com.jcb.moviesapp.data.models.SpokenLanguageModel
import com.jcb.moviesapp.domain.models.Collection
import com.jcb.moviesapp.domain.models.Genre
import com.jcb.moviesapp.domain.models.MovieDetails
import com.jcb.moviesapp.domain.models.ProductionCompany
import com.jcb.moviesapp.domain.models.ProductionCountry
import com.jcb.moviesapp.domain.models.SpokenLanguage

fun MovieDetailsModel.toMovieDetails(): MovieDetails {
    return MovieDetails(
        id = this.id,
        overview = this.overview,
        popularity = this.popularity,
        revenue = this.revenue,
        runtime = this.runtime,
        status = this.status,
        tagline = this.tagline,
        title = this.title,
        video = this.video,
        adult = this.adult,
        budget = this.budget,
        genres = this.genres.map { it.toGenre() },
        homepage = this.homepage,
        backdropPath = this.backdropPath,
        belongsToCollection = this.belongsToCollection?.toCollection(),
        imdbId = this.imdbId,
        originCountry = this.originCountry,
        originalLanguage = this.originalLanguage,
        originalTitle = this.originalTitle,
        posterPath = this.posterPath,
        productionCompanies = this.productionCompanies.map { it.toProductionCompany() },
        productionCountries = this.productionCountries.map { it.toProductionCountry() },
        releaseDate = this.releaseDate,
        spokenLanguages = this.spokenLanguages.map { it.toSpokenLanguage() },
        voteAverage = this.voteAverage,
        voteCount = this.voteCount
    )
}

fun CollectionModel.toCollection(): Collection {
    return Collection(
        id = this.id,
        name = this.name,
        posterPath = this.posterPath,
        backdropPath = this.backdropPath
    )
}

fun GenreModel.toGenre(): Genre {
    return Genre(
        id = this.id,
        name = this.name
    )
}

fun ProductionCompanyModel.toProductionCompany(): ProductionCompany {
    return ProductionCompany(
        id = this.id,
        name = this.name,
        logoPath = this.logoPath,
        originCountry = this.originCountry
    )
}

fun ProductionCountryModel.toProductionCountry(): ProductionCountry {
    return ProductionCountry(
        name = this.name,
        iso = this.iso
    )
}

fun SpokenLanguageModel.toSpokenLanguage(): SpokenLanguage {
    return SpokenLanguage(
        name = this.name,
        englishName = this.englishName,
        iso = this.iso
    )
}