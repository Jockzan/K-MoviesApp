package com.jcb.moviesapp.data.utils

import java.util.Locale

/**
 * Utility object for handling locale-specific logic, such as determining the language for API requests.
 */
object LocalUtils {

    /**
     * Determines the language to be used for API calls based on the default locale of the device.
     *
     * - If the device language is Spanish ("es"), it returns "es-ES".
     * - For all other languages, it defaults to English ("en-US").
     *
     * Example:
     * - Device locale: Spanish -> Output: "es-ES"
     * - Device locale: French -> Output: "en-US" (default)
     *
     * @return The language string formatted for API requests.
     */
    fun getApiLanguage(): String {
        return when (Locale.getDefault().language) {
            "es" -> "es-ES" // Return Spanish locale for API.
            else -> "en-US" // Default to English locale for API.
        }
    }
}
