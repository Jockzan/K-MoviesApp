package com.jcb.moviesapp.data.mappers

import com.jcb.moviesapp.data.api.network.NetworkError
import com.jcb.moviesapp.data.api.network.RepositoryResult

fun <T> NetworkError.mapErrorRepositoryResult() = RepositoryResult<T>(
    errorCode = errorCode ?: "N/A",
    errorMessage = ensureErrorMessage
)