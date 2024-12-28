package br.com.coutomariel.loansimulator_api.api.endpoint.loan.request

import br.com.coutomariel.loansimulator_api.api.validation.ValidDate
import br.com.coutomariel.loansimulator_api.domain.model.LoanSimulation
import br.com.coutomariel.loansimulator_api.utils.DateProperties.DATE_FORMAT_PATTERN
import br.com.coutomariel.loansimulator_api.utils.toLocalDate
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

@Schema(description = "Loan simulation request")
data class LoanSimulationRequest(
    @Schema(description = "Value request to Loan", example = "6000.66", nullable = false)
    @field:NotNull val value: BigDecimal? = null,

    @Schema(description = "Requester Date birth", example = "23/10/1940", nullable = false, format = DATE_FORMAT_PATTERN)
    @field:NotNull @field:ValidDate val dateBirth: String? = null,

    @Schema(description = "Duration in months to payment", example = "60", nullable = false)
    @field:NotNull val durationInMonths: Int? = null
) {
    fun toModel() = LoanSimulation(
        value = this.value!!,
        dateBirth = this.dateBirth!!.toLocalDate(),
        durationInMonths = this.durationInMonths!!
    )
}
