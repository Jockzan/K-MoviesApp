package com.jcb.moviesapp.domain.repositories

import com.jcb.moviesapp.data.api.network.RepositoryResult
import com.jcb.moviesapp.domain.models.MovieDetails
import com.jcb.moviesapp.domain.models.MovieVideos

interface DetailsRepository {
    suspend fun getMovieDetails(id: Int): RepositoryResult<MovieDetails>

    suspend fun getMovieVideos(id: Int): RepositoryResult<MovieVideos>
}
