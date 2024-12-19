package com.jcb.moviesapp.data.api.network

import com.squareup.moshi.Json

enum class NetworkErrorType {
    CONNECTION_ERROR,
    TIMEOUT_ERROR,
    API_ERROR,
    UNKNOWN_ERROR,
    NO_CONTENT_ERROR,
}

data class APIError(
    val errors: List<Error>?,
    val validation: Validation?,
    @Json(name = "Message") val message: String?,
)

data class Error(
    val code: String,
    val message: String,
)

data class Validation(
    val type: List<Error>?,
    val provider: List<Error>?,
)

data class NetworkError(
    var type: NetworkErrorType,
    var rawError: String? = null,
    var errorCode: String? = null,
    var apiError: APIError? = null,
) {
    val ensureErrorMessage: String
        get() {
            apiError?.let { error ->
                return error.errors?.joinToString("\n") {
                    it.message
                } ?: error.message.orEmpty()
            }

            rawError?.let {
                return it
            }

            return type.name
        }
}