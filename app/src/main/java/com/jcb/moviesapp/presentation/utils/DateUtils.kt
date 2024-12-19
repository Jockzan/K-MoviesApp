package com.jcb.moviesapp.presentation.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Utility object for handling date-related operations such as formatting and calculating date ranges.
 */
object DateUtils {

    // The standard date format used for parsing and formatting dates.
    private const val DATE_FORMAT = "yyyy-MM-dd"

    // A DateTimeFormatter instance based on the standard date format.
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT)

    /**
     * Converts a date string in the standard format ("yyyy-MM-dd") to a localized medium format.
     *
     * Example:
     * Input: "2023-12-18"
     * Output: "Dec 18, 2023" (depending on the locale)
     *
     * @receiver The date string in "yyyy-MM-dd" format.
     * @return The date string formatted in a medium localized style.
     */
    fun String.toMedium(): String {
        val inputFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
        val outputFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        val localDate = LocalDate.parse(this, inputFormatter)
        return outputFormatter.format(localDate)
    }

    /**
     * Calculates a future release date range starting one week from the base date
     * and ending six months after that.
     *
     * Example:
     * Base Date: "2023-12-01"
     * Output: ("2023-12-08", "2024-06-08")
     *
     * @param baseDate The starting date for the range calculation. Defaults to the current date.
     * @return A pair containing the formatted start and end dates of the range.
     */
    fun getFutureReleaseDates(baseDate: LocalDate = LocalDate.now()): Pair<String, String> {
        val minDate = baseDate.plusWeeks(1)
        val maxDate = minDate.plusMonths(6)

        return Pair(minDate.format(formatter), maxDate.format(formatter))
    }

    /**
     * Calculates the date range for the current week, starting on Monday and ending on Sunday.
     *
     * Example:
     * Base Date: "2023-12-06" (Wednesday)
     * Output: ("2023-12-04", "2023-12-10") (Monday to Sunday of the same week)
     *
     * @param baseDate The base date for calculating the current week. Defaults to the current date.
     * @return A pair containing the formatted start and end dates of the week.
     */
    fun getNowPlayingDates(baseDate: LocalDate = LocalDate.now()): Pair<String, String> {
        val startOfWeek = baseDate.minusDays(baseDate.dayOfWeek.value.toLong() - 1)
        val endOfWeek = startOfWeek.plusDays(6)

        return Pair(startOfWeek.format(formatter), endOfWeek.format(formatter))
    }
}
