package br.com.coutomariel.loansimulator_api.utils

import br.com.coutomariel.loansimulator_api.utils.DateProperties.DATE_FORMATTER
import java.time.LocalDate
import java.time.format.DateTimeParseException

/**
 * Converts a string in the format "dd/MM/yyyy" to a [LocalDate].
 * @throws IllegalArgumentException if the string is not a valid date.
 * @return the [LocalDate] representation of the string.
 */
fun String.toLocalDate(): LocalDate {
    return try {
        LocalDate.parse(this, DATE_FORMATTER)
    } catch (e: DateTimeParseException) {
        throw IllegalArgumentException("Error parsing string ($this) to LocalDate.")
    }
}
