package br.com.coutomariel.loansimulator_api.domain.model

import java.math.BigDecimal
import java.time.LocalDate

class LoanSimulation(
    val value: BigDecimal,
    val dateBirth: LocalDate,
    val durationInMonths: Int
)
