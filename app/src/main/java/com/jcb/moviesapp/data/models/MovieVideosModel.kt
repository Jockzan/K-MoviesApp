package com.jcb.moviesapp.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieVideosModel(
    val id: Int,
    val results: List<VideoModel>,
)

@JsonClass(generateAdapter = true)
data class VideoModel(
    val id: String,
    val name: String,
    val key: String,
    val site: String,
    val size: Int,
    val type: String,
    val official: Boolean,
    @Json(name = "published_at") val publishedAt: String,
    @Json(name = "iso_639_1") val iso6391: String,
    @Json(name = "iso_3166_1") val iso31661: String,
)
