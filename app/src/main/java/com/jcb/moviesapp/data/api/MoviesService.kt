package com.jcb.moviesapp.data.api

import com.jcb.moviesapp.data.models.GenreListModel
import com.jcb.moviesapp.data.models.MovieDetailsModel
import com.jcb.moviesapp.data.models.MovieListModel
import com.jcb.moviesapp.data.models.MovieVideosModel
import com.jcb.moviesapp.presentation.utils.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {

    @GET("discover/movie")
    suspend fun discoverMovies(
        @HeaderMap headers: Map<String, String>,
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false,
        @Query("language") language: String = "en-US",
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("without_genres") withoutGenres: String? = null,
        @Query("vote_count.gte") voteCountGte: Int? = null,
        @Query("with_release_type") withReleaseType: String? = null,
        @Query("release_date.gte") minReleaseDate: String? = null,
        @Query("release_date.lte") maxReleaseDate: String? = null
    ): MovieListModel

    @GET("genre/movie/list")
    suspend fun getMovieGenres(
        @HeaderMap headers: Map<String, String>,
        @Query("language") language: String = "en-US"
    ): GenreListModel

    @GET("movie/{id}/videos")
    suspend fun getMovieVideos(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id: Int,
    ): MovieVideosModel

    @GET("movie/{id}")
    suspend fun getMovieDetails(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id: Int,
        @Query("language") language: String = "en-US",
    ): MovieDetailsModel
}

fun createMoviesService() = Retrofit
    .Builder()
    .baseUrl(Constants.API_URL)
    .addConverterFactory(
        MoshiConverterFactory.create(
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        )
    )
    .client(
        OkHttpClient
            .Builder()
            .addNetworkInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                },
            ).retryOnConnectionFailure(true)
            .build(),
    ).build()
    .create(MoviesService::class.java) as MoviesService
