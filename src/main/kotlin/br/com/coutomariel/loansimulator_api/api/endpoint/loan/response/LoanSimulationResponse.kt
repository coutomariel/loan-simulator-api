package br.com.coutomariel.loansimulator_api.api.endpoint.loan.response

import br.com.coutomariel.loansimulator_api.domain.model.LoanSimulationResult
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

@Schema(description = "Loan simulation response")
data class LoanSimulationResponse(
    @Schema(description = "Total value to loan payment.", example = "16000.66", nullable = false)
    val totalValue: BigDecimal,
    @Schema(description = "Monthly installment value.", example = "16000.66", nullable = false)
    val monthlyInstallment: BigDecimal,
    @Schema(description = "Loan interestRate percent", example = "4.0", nullable = false)
    val interestRate: BigDecimal
) {
    companion object {
        fun from(result: LoanSimulationResult) = LoanSimulationResponse(
            totalValue = result.totalValue, result.monthlyInstallment, interestRate = result.interestRate
        )
    }
}
