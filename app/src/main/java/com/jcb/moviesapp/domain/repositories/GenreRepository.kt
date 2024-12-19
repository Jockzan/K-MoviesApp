package com.jcb.moviesapp.domain.repositories

import com.jcb.moviesapp.data.api.network.RepositoryResult
import com.jcb.moviesapp.domain.models.Genre

interface GenreRepository {
    suspend fun getGenres(): RepositoryResult<List<Genre>>
}