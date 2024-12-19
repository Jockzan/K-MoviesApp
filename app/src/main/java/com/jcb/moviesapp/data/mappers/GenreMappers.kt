package com.jcb.moviesapp.data.mappers

import com.jcb.moviesapp.data.models.GenreListModel
import com.jcb.moviesapp.domain.models.Genre

fun GenreListModel.toGenreList(): List<Genre> {
    return this.genres.map { it.toGenre() }
}