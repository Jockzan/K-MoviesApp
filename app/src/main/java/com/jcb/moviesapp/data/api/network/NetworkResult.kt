package com.jcb.moviesapp.data.api.network

class NetworkResult<T>(
    val result: T? = null,
    val networkError: NetworkError? = null,
) {
    val isError: Boolean
        get() = networkError != null

    val requiredResult: T
        get() = result!!

    val requiredError: NetworkError
        get() = networkError!!
}