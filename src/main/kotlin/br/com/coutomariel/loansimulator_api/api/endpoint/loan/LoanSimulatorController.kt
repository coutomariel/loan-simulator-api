package br.com.coutomariel.loansimulator_api.api.endpoint.loan

import br.com.coutomariel.loansimulator_api.api.endpoint.loan.request.LoanSimulationRequest
import br.com.coutomariel.loansimulator_api.api.endpoint.loan.response.LoanSimulationResponse
import br.com.coutomariel.loansimulator_api.domain.service.loan.LoanSimulationService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/loan-simulation")
class LoanSimulatorController(
    val loanSimulationService: LoanSimulationService
) : LoanSimulatorContract {

    private val logger = LoggerFactory.getLogger(LoanSimulatorController::class.java)

    @PostMapping
    @ResponseStatus(OK)
    override fun submitLoanSimulation(
        @RequestBody @Valid loanSimulationRequest: LoanSimulationRequest
    ): LoanSimulationResponse {
        logger.info("Loan simulation request received: ($loanSimulationRequest)")
        val result = loanSimulationService.simulate(loanSimulationRequest.toModel())
        return LoanSimulationResponse.from(result)
    }
}
