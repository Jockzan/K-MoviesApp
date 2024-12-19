package com.jcb.moviesapp.presentation.ui.movies.list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jcb.moviesapp.domain.models.Movie
import com.jcb.moviesapp.domain.repositories.GenreRepository
import com.jcb.moviesapp.domain.repositories.MoviesRepository
import com.jcb.moviesapp.presentation.utils.DateUtils.getFutureReleaseDates
import com.jcb.moviesapp.presentation.utils.DateUtils.getNowPlayingDates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel for managing movie data, genres, and UI state.
 * Handles fetching, filtering, and error handling for movies in different categories.
 */
@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val genreRepository: GenreRepository
) : ViewModel() {

    companion object {
        const val CATEGORY_POPULAR = "popular"
        const val CATEGORY_NOW_PLAYING = "nowPlaying"
        const val CATEGORY_TOP_RATED = "topRated"
        const val CATEGORY_UPCOMING = "upcoming"
        private const val SEARCH_DEBOUNCE_DELAY = 300L
    }

    // Stores the current search query
    private var searchQuery = ""

    // Maps genre IDs to names for quick lookup
    private val genreMap = mutableMapOf<Int, String>()

    // Maps for holding movies and their filtered results by category
    private var moviesMap = mutableMapOf(
        CATEGORY_POPULAR to mutableStateOf(listOf<Movie>()),
        CATEGORY_NOW_PLAYING to mutableStateOf(listOf<Movie>()),
        CATEGORY_TOP_RATED to mutableStateOf(listOf<Movie>()),
        CATEGORY_UPCOMING to mutableStateOf(listOf<Movie>())
    )
    val filteredMoviesMap = mutableMapOf(
        CATEGORY_POPULAR to mutableStateOf(listOf<Movie>()),
        CATEGORY_NOW_PLAYING to mutableStateOf(listOf<Movie>()),
        CATEGORY_TOP_RATED to mutableStateOf(listOf<Movie>()),
        CATEGORY_UPCOMING to mutableStateOf(listOf<Movie>())
    )

    // Maps to track loading and error states for each category
    val showErrorMap = mutableMapOf(
        CATEGORY_POPULAR to mutableStateOf(false),
        CATEGORY_NOW_PLAYING to mutableStateOf(false),
        CATEGORY_TOP_RATED to mutableStateOf(false),
        CATEGORY_UPCOMING to mutableStateOf(false)
    )
    val isLoadingMap = mutableMapOf(
        CATEGORY_POPULAR to mutableStateOf(false),
        CATEGORY_NOW_PLAYING to mutableStateOf(false),
        CATEGORY_TOP_RATED to mutableStateOf(false),
        CATEGORY_UPCOMING to mutableStateOf(false)
    )

    // Tracks the current page for pagination
    private val pageMap = mutableMapOf(
        CATEGORY_POPULAR to 1,
        CATEGORY_NOW_PLAYING to 1,
        CATEGORY_TOP_RATED to 1,
        CATEGORY_UPCOMING to 1
    )

    // Stores date ranges for categories with date filters
    private val dateMap = mutableMapOf<String, Pair<String, String>>()

    // Job for managing debounced search queries
    private var searchJob: Job? = null

    // Initializes the ViewModel by fetching genres and movies for all categories
    init {
        fetchGenres()
        fetchMovies(CATEGORY_POPULAR)
        fetchMovies(CATEGORY_NOW_PLAYING, ::getNowPlayingDates)
        fetchMovies(CATEGORY_TOP_RATED)
        fetchMovies(CATEGORY_UPCOMING, ::getFutureReleaseDates)
    }

    /**
     * Fetches genres and maps their IDs to names.
     */
    private fun fetchGenres() = viewModelScope.launch {
        val result = genreRepository.getGenres()
        if (!result.isError) {
            genreMap.clear()
            genreMap.putAll(result.requireResult.associateBy({ it.id }, { it.name }))
        }
    }

    /**
     * Returns the genre name for a given ID.
     * If the ID is not found, returns "Unknown".
     */
    fun getGenreNameById(id: Int): String = genreMap[id] ?: "Unknown"

    /**
     * Fetches movies for a given category.
     *
     * @param category The movie category (e.g., popular, nowPlaying).
     * @param dateProvider Optional provider for date ranges (e.g., for now playing or upcoming movies).
     */
    fun fetchMovies(
        category: String,
        dateProvider: (() -> Pair<String, String>)? = null
    ) = viewModelScope.launch {
        val isLoading = isLoadingMap[category] ?: return@launch
        if (isLoading.value) return@launch

        isLoading.value = true
        val page = pageMap[category] ?: 1

        // Fetch or compute the date range for the category
        val (minDate, maxDate) = dateMap[category] ?: dateProvider?.invoke() ?: Pair("", "")
        dateMap[category] = minDate to maxDate

        // Perform the API call based on the category
        val result = withContext(Dispatchers.IO) {
            when (category) {
                CATEGORY_TOP_RATED -> moviesRepository.getTopRatedMovies(page)
                CATEGORY_POPULAR -> moviesRepository.getPopularMovies(page)
                CATEGORY_NOW_PLAYING, CATEGORY_UPCOMING -> {
                    if (minDate.isEmpty() || maxDate.isEmpty()) return@withContext null
                    moviesRepository.getMoviesByDateRange(page, minDate, maxDate)
                }
                else -> null
            }
        }

        // Handle the API response and update state
        if (result == null || result.isError) {
            showErrorMap[category]?.value = true
        } else {
            showErrorMap[category]?.value = false
            val movies = result.requireResult.results
            val updatedMovies = (moviesMap[category]?.value.orEmpty() + movies).distinct()
            moviesMap[category]?.value = updatedMovies
            filteredMoviesMap[category]?.value = applyFilter(searchQuery, updatedMovies)
            if (movies.isNotEmpty()) pageMap[category] = page + 1
        }

        isLoading.value = false
    }

    /**
     * Updates the current search query and filters the movies for all categories.
     *
     * @param query The new search query.
     */
    fun updateSearchQuery(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchQuery = query
            moviesMap.forEach { (category, movies) ->
                filteredMoviesMap[category]?.value = applyFilter(query, movies.value)
            }
        }
    }

    /**
     * Filters a list of movies based on a search query.
     *
     * @param query The search query.
     * @param movies The list of movies to filter.
     * @return A filtered list of movies that match the query.
     */
    private fun applyFilter(query: String, movies: List<Movie>): List<Movie> {
        val lowerCaseQuery = query.trim().lowercase()
        return movies.filter { it.title.contains(lowerCaseQuery, true) }
    }
}
