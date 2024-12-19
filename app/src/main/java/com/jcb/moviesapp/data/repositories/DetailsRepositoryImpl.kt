package com.jcb.moviesapp.data.repositories

import com.jcb.moviesapp.BuildConfig
import com.jcb.moviesapp.data.api.MoviesService
import com.jcb.moviesapp.data.api.network.NetworkInteractor
import com.jcb.moviesapp.data.api.network.RepositoryResult
import com.jcb.moviesapp.data.mappers.mapErrorRepositoryResult
import com.jcb.moviesapp.data.mappers.toMovieDetails
import com.jcb.moviesapp.data.mappers.toMovieVideos
import com.jcb.moviesapp.data.utils.LocalUtils
import com.jcb.moviesapp.domain.models.MovieDetails
import com.jcb.moviesapp.domain.models.MovieVideos
import com.jcb.moviesapp.domain.repositories.DetailsRepository

/**
 * Implementation of the [DetailsRepository] interface for handling movie details and video-related API calls.
 * It uses [MoviesService] for API requests and [NetworkInteractor] for safe network operations.
 */
class DetailsRepositoryImpl(
    private val networkInteractor: NetworkInteractor,
    private val moviesService: MoviesService,
) : DetailsRepository {

    /**
     * Lazy initialization of API headers, including the authorization token.
     */
    private val headers by lazy {
        mapOf("Authorization" to "Bearer ${BuildConfig.API_TOKEN}")
    }

    /**
     * Fetches the details of a specific movie by its ID.
     *
     * @param id The ID of the movie.
     * @return [RepositoryResult] containing the [MovieDetails] or an error.
     * - On success, the API response is mapped to a domain model using [toMovieDetails].
     * - On error, the network error is mapped to a repository-level error using [mapErrorRepositoryResult].
     */
    override suspend fun getMovieDetails(id: Int): RepositoryResult<MovieDetails> {
        val result = networkInteractor.safeApiCall {
            moviesService.getMovieDetails(
                headers = headers,
                id = id,
                language = LocalUtils.getApiLanguage() // Determines the API language dynamically.
            )
        }
        if (result.isError) {
            // Maps the network error to a repository-specific error.
            return result.requiredError.mapErrorRepositoryResult()
        }
        // Converts the successful response into the domain model.
        return RepositoryResult(
            result.requiredResult.toMovieDetails()
        )
    }

    /**
     * Fetches the videos (e.g., trailers) associated with a specific movie by its ID.
     *
     * @param id The ID of the movie.
     * @return [RepositoryResult] containing the [MovieVideos] or an error.
     * - On success, the API response is mapped to a domain model using [toMovieVideos].
     * - On error, the network error is mapped to a repository-level error using [mapErrorRepositoryResult].
     */
    override suspend fun getMovieVideos(id: Int): RepositoryResult<MovieVideos> {
        val result = networkInteractor.safeApiCall {
            moviesService.getMovieVideos(headers, id)
        }
        if (result.isError) {
            // Maps the network error to a repository-specific error.
            return result.requiredError.mapErrorRepositoryResult()
        }
        // Converts the successful response into the domain model.
        return RepositoryResult(
            result.requiredResult.toMovieVideos()
        )
    }
}
