package br.com.coutomariel.loansimulator_api.domain.service.loan

import br.com.coutomariel.loansimulator_api.domain.exception.BirthDateNotValidException
import br.com.coutomariel.loansimulator_api.domain.exception.InterestRateNotFoundException
import br.com.coutomariel.loansimulator_api.domain.model.LoanSimulation
import br.com.coutomariel.loansimulator_api.infrastructure.postgres.repository.InterestRateRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
class LoanSimulationServiceTest {

    @MockK
    private lateinit var interestRateRepository: InterestRateRepository

    @MockK
    private lateinit var loanCalculator: LoanCalculator

    @InjectMockKs
    private lateinit var loanSimulationService: LoanSimulationService

    @Test
    fun `should return loan simulation when valid request`() {
        val ageOfRequester = 28
        val mockInterestRate = 0.03
        val mockMonthlyInstallment = BigDecimal.valueOf(473.55)

        every { interestRateRepository.findByAge(any()) } returns mockInterestRate
        every { loanCalculator.calculate(any(), any(), any()) } returns mockMonthlyInstallment

        val loanSimulation = LoanSimulation(
            value = BigDecimal.valueOf(10000.00),
            dateBirth = LocalDate.now().minusYears(ageOfRequester.toLong()),
            durationInMonths = 24
        )
        val expectedTotalValue = mockMonthlyInstallment.multiply(loanSimulation.durationInMonths.toBigDecimal())

        val result = loanSimulationService.simulate(loanSimulation)
        with(result) {
            assertEquals(expectedTotalValue, this.totalValue)
            assertEquals(mockMonthlyInstallment, this.monthlyInstallment)
            assertEquals(mockInterestRate, this.interestRate.toDouble())
        }
        verify(exactly = 1) { interestRateRepository.findByAge(ageOfRequester) }
        verify(exactly = 1) { loanCalculator.calculate(any(), any(), any()) }
    }

    @Test
    fun `should throw birth date not valid exception exception when it was date future`() {
        val loanSimulation = LoanSimulation(
            value = BigDecimal.valueOf(10000.00),
            dateBirth = LocalDate.now().plusMonths(5),
            durationInMonths = 24
        )

        val expectedErrorMessage = "Birth date can't greater than today"
        val error = assertThrows<BirthDateNotValidException> {
            loanSimulationService.simulate(loanSimulation)
        }
        assertEquals(expectedErrorMessage, error.message)

        verify(exactly = 0) { interestRateRepository.findByAge(any()) }
        verify(exactly = 0) { loanCalculator.calculate(any(), any(), any()) }
    }

    @Test
    fun `should throw interest rate not found exception exception when it theres not in database`() {
        val loanSimulation = LoanSimulation(
            value = BigDecimal.valueOf(10000.00),
            dateBirth = LocalDate.now().minusYears(32),
            durationInMonths = 24
        )
        val expectedErrorMessage = "Interest rate not found."
        val expectedException = InterestRateNotFoundException(expectedErrorMessage)
        every { interestRateRepository.findByAge(any()) } returns null

        val error = assertThrows<InterestRateNotFoundException> {
            loanSimulationService.simulate(loanSimulation)
        }
        assertEquals(expectedErrorMessage, error.message)

        verify(exactly = 1) { interestRateRepository.findByAge(any()) }
        verify(exactly = 0) { loanCalculator.calculate(any(), any(), any()) }
    }
}
