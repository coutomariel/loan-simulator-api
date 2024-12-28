package br.com.coutomariel.loansimulator_api.utils

import java.time.format.DateTimeFormatter

object DateProperties {
    const val DATE_FORMAT_PATTERN: String = "dd/MM/yyyy"
    val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN)
}
