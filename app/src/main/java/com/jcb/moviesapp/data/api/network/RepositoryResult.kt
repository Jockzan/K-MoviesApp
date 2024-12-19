package com.jcb.moviesapp.data.api.network

enum class RepositoryErrorType {
    NOT_DEFINED,
    CONNECTION_ERROR,
    API_ERROR,
    UNKNOWN_ERROR,
    NO_CONTENT_ERROR,
}

data class RepositoryError(
    val code: String,
    val message: String,
    val errorType: RepositoryErrorType = RepositoryErrorType.NOT_DEFINED,
) {
    val hasServerCode get() = code != "N/A"
}

data class RepositoryResult<T>(
    val result: T? = null,
    val error: RepositoryError? = null,
) {

    constructor(
        errorCode: String,
        errorMessage: String,
        errorType: RepositoryErrorType = RepositoryErrorType.NOT_DEFINED,
    ) : this(
        error = RepositoryError(
            errorCode,
            errorMessage,
            errorType
        )
    )

    val isError: Boolean
        get() = error != null

    val requireResult: T
        get() = result!!

    val requireError: RepositoryError
        get() = error!!
}
