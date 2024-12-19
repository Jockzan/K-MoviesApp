package com.jcb.moviesapp.domain.repositories

import com.jcb.moviesapp.data.api.network.RepositoryResult
import com.jcb.moviesapp.domain.models.MovieList

interface MoviesRepository {
    suspend fun getPopularMovies(page: Int): RepositoryResult<MovieList>
    suspend fun getTopRatedMovies(page: Int): RepositoryResult<MovieList>
    suspend fun getMoviesByDateRange(
        page: Int,
        minReleaseDate: String,
        maxReleaseDate: String
    ): RepositoryResult<MovieList>
}
