package com.jcb.moviesapp.domain.models

data class MovieVideos(
    val id: Int,
    val results: List<Video>,
)

data class Video(
    val id: String,
    val name: String,
    val key: String,
    val site: String,
    val size: Int,
    val type: String,
    val official: Boolean,
    val publishedAt: String,
    val iso6391: String,
    val iso31661: String,
)
