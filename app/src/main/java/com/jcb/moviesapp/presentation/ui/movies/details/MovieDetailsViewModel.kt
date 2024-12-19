package com.jcb.moviesapp.presentation.ui.movies.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jcb.moviesapp.domain.models.MovieDetails
import com.jcb.moviesapp.domain.models.Video
import com.jcb.moviesapp.domain.repositories.DetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel for managing the movie details and videos.
 * It interacts with [DetailsRepository] to fetch data and manages the UI state.
 */
@HiltViewModel
class MovieDetailsViewModel
@Inject constructor(
    private val detailsRepository: DetailsRepository,
) : ViewModel() {

    // State holding the list of movie videos (e.g., trailers).
    var movieVideos by mutableStateOf<List<Video>>(listOf())

    // State holding the details of a specific movie.
    var movieDetails by mutableStateOf<MovieDetails?>(null)

    // State indicating whether an error occurred while fetching data.
    var showError by mutableStateOf(false)

    /**
     * Fetches the details of a specific movie.
     *
     * @param movieId The ID of the movie (default: 1).
     * The data is fetched on an IO thread to avoid blocking the main thread.
     * Updates [movieDetails] on success and [showError] on failure.
     */
    fun getMovieDetails(movieId: Int = 1) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val result = detailsRepository.getMovieDetails(movieId)
            if (result.isError) {
                showError = true // Show error if the API call fails.
            } else {
                showError = false // Reset error state if the call succeeds.
                movieDetails = result.requireResult // Update movie details.
            }
        }
    }

    /**
     * Fetches the videos (e.g., trailers) associated with a specific movie.
     *
     * @param movieId The ID of the movie (default: 1).
     * The data is fetched on an IO thread to avoid blocking the main thread.
     * Updates [movieVideos] with the results or an empty list if the API call fails.
     */
    fun getMovieVideos(movieId: Int = 1) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val result = detailsRepository.getMovieVideos(movieId)
            movieVideos = if (result.isError) {
                emptyList() // If the call fails, show an empty list of videos.
            } else {
                result.requireResult.results // Populate the list with API results.
            }
        }
    }
}
