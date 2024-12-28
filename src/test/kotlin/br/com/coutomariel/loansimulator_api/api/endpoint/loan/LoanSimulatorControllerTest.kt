package br.com.coutomariel.loansimulator_api.api.endpoint.loan

import br.com.coutomariel.loansimulator_api.api.endpoint.loan.request.LoanSimulationRequest
import br.com.coutomariel.loansimulator_api.domain.model.LoanSimulationResult
import br.com.coutomariel.loansimulator_api.domain.service.loan.LoanCalculator
import br.com.coutomariel.loansimulator_api.infrastructure.postgres.repository.InterestRateRepository
import br.com.coutomariel.loansimulator_api.utils.toLocalDate
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LoanSimulatorControllerTest {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var loanCalculator: LoanCalculator

    @Autowired
    private lateinit var interestRateRepository: InterestRateRepository

    @Test
    fun `should return simulation result when request is valid`() {
        val request = LoanSimulationRequest(
            value = BigDecimal.valueOf(10000.00),
            dateBirth = "01/07/1986",
            durationInMonths = 24
        )
        val age = Period.between(request.dateBirth!!.toLocalDate(), LocalDate.now()).years
        val interestRate = interestRateRepository.findByAge(age)!!.toBigDecimal()

        val expectedMonthlyInstallment = loanCalculator
            .calculate(request.value!!, request.durationInMonths!!, interestRate)

        val expectedTotalValue = expectedMonthlyInstallment.multiply(request.durationInMonths!!.toBigDecimal())

        mockMvc.perform(
            post("/api/v1/loan-simulation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.totalValue").value(expectedTotalValue))
            .andExpect(jsonPath("$.monthlyInstallment").value(expectedMonthlyInstallment))
            .andExpect(jsonPath("$.interestRate").value(mockLoanSimulationResult().interestRate))
    }

    @Test
    fun `should return bad request when value is null`() {
        val request = mockLoanSimulationRequest().copy(value = null)
        mockMvc.perform(
            post("/api/v1/loan-simulation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.errorFields.size()").value(1))
            .andExpect(jsonPath("$.errorFields.[0].field").value("value"))
            .andExpect(jsonPath("$.errorFields.[0].message").value("must not be null"))
    }

    @Test
    fun `should return bad request when format birth date is not valid`() {
        val request = mockLoanSimulationRequest().copy(dateBirth = "1986-07-01")
        mockMvc.perform(
            post("/api/v1/loan-simulation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.errorFields.size()").value(1))
            .andExpect(jsonPath("$.errorFields.[0].field").value("dateBirth"))
            .andExpect(
                jsonPath("$.errorFields.[0].message")
                    .value("Date format invalid. Use format:(dd/MM/yyyy)")
            )
    }

    @Test
    fun `should return bad request when format birth date is future date`() {
        val request = mockLoanSimulationRequest()
            .copy(dateBirth = "01/07/${LocalDate.now().plusMonths(5).year}")
        mockMvc.perform(
            post("/api/v1/loan-simulation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.errorFields.size()").value(1))
            .andExpect(jsonPath("$.errorFields.[0].field").value("birthDate"))
            .andExpect(
                jsonPath("$.errorFields.[0].message")
                    .value("Birth date can't greater than today")
            )
    }

    @Test
    fun `should return bad request when format birth date is null`() {
        val request = mockLoanSimulationRequest().copy(dateBirth = null)
        mockMvc.perform(
            post("/api/v1/loan-simulation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.errorFields.size()").value(1))
            .andExpect(jsonPath("$.errorFields.[0].field").value("dateBirth"))
            .andExpect(jsonPath("$.errorFields.[0].message").value("must not be null"))
    }

    @Test
    fun `should return unprocessable entity when value is negative`() {
        val request = mockLoanSimulationRequest().copy(value = BigDecimal.valueOf(-10000.00))
        mockMvc.perform(
            post("/api/v1/loan-simulation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.httpCode").value(422))
    }

    private fun mockLoanSimulationRequest() = LoanSimulationRequest(
        value = BigDecimal.valueOf(10000.0), dateBirth = "23/10/1940", durationInMonths = 24
    )

    private fun mockLoanSimulationResult() = LoanSimulationResult(
        totalValue = BigDecimal.valueOf(100000.00),
        monthlyInstallment = BigDecimal.valueOf(470.00),
        interestRate = BigDecimal.valueOf(0.03)
    )
}
