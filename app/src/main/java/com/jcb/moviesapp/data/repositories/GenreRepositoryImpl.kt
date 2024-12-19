package com.jcb.moviesapp.data.repositories

import com.jcb.moviesapp.BuildConfig
import com.jcb.moviesapp.data.api.MoviesService
import com.jcb.moviesapp.data.api.network.NetworkInteractor
import com.jcb.moviesapp.data.api.network.RepositoryResult
import com.jcb.moviesapp.data.mappers.mapErrorRepositoryResult
import com.jcb.moviesapp.data.mappers.toGenreList
import com.jcb.moviesapp.data.utils.LocalUtils
import com.jcb.moviesapp.domain.models.Genre
import com.jcb.moviesapp.domain.repositories.GenreRepository

/**
 * Implementation of the [GenreRepository] interface for handling genre-related API calls.
 * Communicates with the API through [MoviesService] and processes results using [NetworkInteractor].
 */
class GenreRepositoryImpl(
    private val networkInteractor: NetworkInteractor,
    private val moviesService: MoviesService
) : GenreRepository {

    /**
     * Lazy initialization of API headers, including the authorization token.
     */
    private val headers by lazy {
        mapOf("Authorization" to "Bearer ${BuildConfig.API_TOKEN}")
    }

    /**
     * Fetches the list of movie genres from the API.
     *
     * @return [RepositoryResult] containing a list of [Genre] objects or an error.
     * - On success, maps the API response to a domain model using [toGenreList].
     * - On error, maps the network error to a repository-level error using [mapErrorRepositoryResult].
     */
    override suspend fun getGenres(): RepositoryResult<List<Genre>> {
        val result = networkInteractor.safeApiCall {
            // Calls the API to fetch the list of genres.
            moviesService.getMovieGenres(
                headers = headers,
                language = LocalUtils.getApiLanguage() // Determines the language based on user settings.
            )
        }
        if (result.isError) {
            // Maps the error to a repository-level error if the API call fails.
            return result.requiredError.mapErrorRepositoryResult()
        }
        // Maps the API response to the domain model and returns it.
        return RepositoryResult(
            result.requiredResult.toGenreList()
        )
    }
}
