package com.jcb.moviesapp.data.api.network

import android.util.Log
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

/**
 * Implementation of the NetworkInteractor interface to safely handle API calls
 * and map exceptions to appropriate network errors.
 */
class NetworkInteractorImpl : NetworkInteractor {

    /**
     * Executes an API call safely within the given dispatcher and handles exceptions.
     *
     * @param dispatcher Coroutine dispatcher to run the API call.
     * @param apiCall The suspend API call to execute.
     * @return A [NetworkResult] containing either the successful result or an error.
     */
    override suspend fun <T> safeApiCall(
        dispatcher: CoroutineDispatcher,
        apiCall: suspend () -> T
    ): NetworkResult<T> =
        withContext(dispatcher) {
            try {
                val response = apiCall.invoke()
                NetworkResult(result = response)
            } catch (throwable: Throwable) {
                val networkError = createError(throwable)
                NetworkResult(networkError = networkError)
            }
        }

    companion object {
        private const val TAG = "NetworkInteractorImpl"

        /**
         * Maps various throwable types to a [NetworkError] with a specific error type and message.
         *
         * @param throwable The throwable exception encountered during the API call.
         * @return A [NetworkError] with the appropriate error type and details.
         */
        fun createError(throwable: Throwable): NetworkError = when (throwable) {
            is ConnectException -> {
                // Handles connection errors (e.g., no internet)
                NetworkError(
                    type = NetworkErrorType.CONNECTION_ERROR,
                    rawError = throwable.message,
                    errorCode = NetworkErrorType.CONNECTION_ERROR.name
                )
            }

            is SocketTimeoutException, is TimeoutException -> {
                // Handles timeout errors
                NetworkError(
                    type = NetworkErrorType.TIMEOUT_ERROR,
                    rawError = throwable.message,
                    errorCode = NetworkErrorType.TIMEOUT_ERROR.name
                )
            }

            is IOException -> {
                // Handles general I/O errors
                NetworkError(
                    type = NetworkErrorType.CONNECTION_ERROR,
                    rawError = throwable.message,
                    errorCode = NetworkErrorType.CONNECTION_ERROR.name
                )
            }

            is HttpException -> {
                // Handles HTTP errors returned by the server
                val bodyResponse: String? = throwable.response()?.errorBody()?.string()
                val apiError = parseErrorBody(throwable)
                val codeError = apiError?.errors?.first()?.code
                NetworkError(
                    type = NetworkErrorType.API_ERROR,
                    rawError = bodyResponse,
                    errorCode = codeError ?: throwable.code().toString(),
                    apiError = apiError
                )
            }

            is JsonDataException -> {
                // Handles JSON parsing errors
                NetworkError(
                    type = NetworkErrorType.API_ERROR,
                    rawError = throwable.message
                )
            }

            is NullPointerException -> {
                // Handles cases where expected content is null
                NetworkError(
                    type = NetworkErrorType.NO_CONTENT_ERROR,
                    rawError = throwable.message
                )
            }

            else -> {
                // Handles unknown or unclassified errors
                NetworkError(
                    type = NetworkErrorType.UNKNOWN_ERROR,
                    rawError = throwable.message
                )
            }
        }

        /**
         * Parses the error body of an HTTP exception to extract detailed error information.
         *
         * @param exception The [HttpException] thrown during the API call.
         * @return An [APIError] object containing detailed error information or null if parsing fails.
         */
        private fun parseErrorBody(exception: HttpException): APIError? {
            return try {
                exception.response()?.errorBody()?.string()?.let {
                    try {
                        // Parse error body using Moshi JSON adapter
                        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                        val jsonAdapter = moshi.adapter(APIError::class.java)
                        return jsonAdapter.fromJson(it)
                    } catch (e: IOException) {
                        // Handles cases where the error body is not properly formatted
                        val response = exception.response()?.raw()
                        val message = exception.response()?.errorBody()?.string()
                        return APIError(
                            errors = listOf(
                                Error(
                                    code = response?.code?.toString().orEmpty(),
                                    message = response?.message.orEmpty()
                                )
                            ),
                            message = message,
                            validation = null
                        )
                    }
                }
            } catch (exception: Exception) {
                // Logs any exception encountered while parsing the error body
                Log.e(TAG, exception.localizedMessage.orEmpty())
                null
            }
        }
    }
}
