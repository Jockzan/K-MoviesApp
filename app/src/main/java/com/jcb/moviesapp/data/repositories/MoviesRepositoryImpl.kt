package com.jcb.moviesapp.data.repositories

import com.jcb.moviesapp.BuildConfig
import com.jcb.moviesapp.data.api.MoviesService
import com.jcb.moviesapp.data.api.network.NetworkInteractor
import com.jcb.moviesapp.data.api.network.RepositoryError
import com.jcb.moviesapp.data.api.network.RepositoryResult
import com.jcb.moviesapp.data.mappers.mapErrorRepositoryResult
import com.jcb.moviesapp.data.mappers.toMovieList
import com.jcb.moviesapp.data.utils.LocalUtils
import com.jcb.moviesapp.domain.models.MovieList
import com.jcb.moviesapp.domain.repositories.MoviesRepository

/**
 * Implementation of the [MoviesRepository] interface to handle API calls related to movies.
 * Uses [NetworkInteractor] to perform safe API calls and map responses or errors to repository results.
 */
class MoviesRepositoryImpl(
    private val networkInteractor: NetworkInteractor,
    private val moviesService: MoviesService,
) : MoviesRepository {

    /**
     * Lazy initialization of API headers, including the authorization token.
     */
    private val headers by lazy {
        mapOf("Authorization" to "Bearer ${BuildConfig.API_TOKEN}")
    }

    private companion object {
        const val SORT_POPULARITY_DESC = "popularity.desc"
        const val SORT_VOTE_AVERAGE_DESC = "vote_average.desc"
        const val WITHOUT_GENRES = "99,10755" // Genres to exclude
        const val VOTE_COUNT_GTE = 200 // Minimum vote count for top-rated movies
        const val RELEASE_TYPE_THEATRICAL_DIGITAL = "2|3" // Theatrical or digital release types
    }

    /**
     * Fetches a list of popular movies from the API.
     *
     * @param page The page number for pagination.
     * @return [RepositoryResult] containing a [MovieList] or an error.
     */
    override suspend fun getPopularMovies(page: Int): RepositoryResult<MovieList> {
        val result = networkInteractor.safeApiCall {
            moviesService.discoverMovies(
                headers = headers,
                page = page,
                sortBy = SORT_POPULARITY_DESC,
                language = LocalUtils.getApiLanguage()
            )
        }
        if (result.isError) {
            return result.requiredError.mapErrorRepositoryResult()
        }
        return RepositoryResult(
            result.requiredResult.toMovieList()
        )
    }

    /**
     * Fetches a list of top-rated movies from the API.
     *
     * @param page The page number for pagination.
     * @return [RepositoryResult] containing a [MovieList] or an error.
     */
    override suspend fun getTopRatedMovies(page: Int): RepositoryResult<MovieList> {
        val result = networkInteractor.safeApiCall {
            moviesService.discoverMovies(
                headers = headers,
                page = page,
                sortBy = SORT_VOTE_AVERAGE_DESC,
                withoutGenres = WITHOUT_GENRES,
                voteCountGte = VOTE_COUNT_GTE,
                language = LocalUtils.getApiLanguage()
            )
        }
        if (result.isError) {
            return result.requiredError.mapErrorRepositoryResult()
        }
        return RepositoryResult(
            result.requiredResult.toMovieList()
        )
    }

    /**
     * Fetches movies within a specific release date range.
     *
     * @param page The page number for pagination.
     * @param minReleaseDate The minimum release date in `yyyy-MM-dd` format.
     * @param maxReleaseDate The maximum release date in `yyyy-MM-dd` format.
     * @return [RepositoryResult] containing a [MovieList] or an error.
     */
    override suspend fun getMoviesByDateRange(
        page: Int,
        minReleaseDate: String,
        maxReleaseDate: String
    ): RepositoryResult<MovieList> {
        // Validates the date range
        if (minReleaseDate > maxReleaseDate) {
            return RepositoryResult(
                error = RepositoryError(
                    message = "Invalid date range: minReleaseDate ($minReleaseDate) is after maxReleaseDate ($maxReleaseDate)",
                    code = ""
                )
            )
        }

        val result = networkInteractor.safeApiCall {
            moviesService.discoverMovies(
                headers = headers,
                page = page,
                sortBy = SORT_POPULARITY_DESC,
                withReleaseType = RELEASE_TYPE_THEATRICAL_DIGITAL,
                minReleaseDate = minReleaseDate,
                maxReleaseDate = maxReleaseDate,
                language = LocalUtils.getApiLanguage()
            )
        }
        if (result.isError) {
            return result.requiredError.mapErrorRepositoryResult()
        }
        return RepositoryResult(
            result.requiredResult.toMovieList()
        )
    }
}
