package br.com.coutomariel.loansimulator_api.domain.model

import java.math.BigDecimal

class LoanSimulationResult(
    val totalValue: BigDecimal,
    val monthlyInstallment: BigDecimal,
    val interestRate: BigDecimal
)
