package br.com.coutomariel.loansimulator_api.api.endpoint.loan

import br.com.coutomariel.loansimulator_api.api.endpoint.loan.request.LoanSimulationRequest
import br.com.coutomariel.loansimulator_api.api.endpoint.loan.response.LoanSimulationResponse
import br.com.coutomariel.loansimulator_api.api.handler.response.ErrorDetailsResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "Loan Simulator API", description = "Loan simulation APIs")
interface LoanSimulatorContract {

    @Operation(
        summary = "Loan simulation request",
        description = "Post a Loan Simulate request. The response is a simulated loan response.",
        // tags = ["Loan simulate", "post"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Loan simulated with success",
                content = [
                    Content(
                        schema = Schema(implementation = LoanSimulationResponse::class),
                        mediaType = "application/json"
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "The Tutorial with g  iven Id was not found.",
                content = [
                    Content(
                        schema = Schema(implementation = ErrorDetailsResponse::class),
                        mediaType = "application/json"
                    )
                ]
            )
        ]
    )
    fun submitLoanSimulation(loanSimulationRequest: LoanSimulationRequest): LoanSimulationResponse
}
