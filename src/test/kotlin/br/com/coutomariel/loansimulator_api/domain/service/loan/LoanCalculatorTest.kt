package br.com.coutomariel.loansimulator_api.domain.service.loan

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.RoundingMode

class LoanCalculatorTest {

    private val loanCalculator = LoanCalculator()

    @Test
    fun `should calculate correct loan amount for valid inputs`() {
        val presentValue = BigDecimal("10000.00")
        val durationInMonths = 12
        val interestRate = BigDecimal("0.05") // 5% annual interest

        val result = loanCalculator.calculate(presentValue, durationInMonths, interestRate)

        val expectedValue = BigDecimal("856.075") // Calculated externally for validation
        assertEquals(expectedValue, result.setScale(3, RoundingMode.HALF_EVEN))
    }

    @Test
    fun `should handle zero interest rate`() {
        val presentValue = BigDecimal("10000.00")
        val durationInMonths = 12
        val interestRate = BigDecimal.ZERO

        val result = loanCalculator.calculate(presentValue, durationInMonths, interestRate)

        val expectedValue = BigDecimal("833.333") // 10000 / 12
        assertEquals(expectedValue, result.setScale(3, RoundingMode.HALF_EVEN))
    }

    @Test
    fun `should throw ArithmeticException for zero duration`() {
        val presentValue = BigDecimal("10000.00")
        val durationInMonths = 0
        val interestRate = BigDecimal("0.05")

        assertThrows(ArithmeticException::class.java) {
            loanCalculator.calculate(presentValue, durationInMonths, interestRate)
        }
    }

    @Test
    fun `should handle large inputs`() {
        val presentValue = BigDecimal("1000000.00")
        val durationInMonths = 360 // 30 years
        val interestRate = BigDecimal("0.03") // 3% annual interest

        val result = loanCalculator.calculate(presentValue, durationInMonths, interestRate)

        val expectedValue = BigDecimal("4216.040") // Calculated externally for validation
        assertEquals(expectedValue, result.setScale(3, RoundingMode.HALF_EVEN))
    }

    @Test
    fun `should throw exception for negative present value`() {
        val presentValue = BigDecimal("-10000.00")
        val durationInMonths = 12
        val interestRate = BigDecimal("0.05")

        val exception = assertThrows(IllegalArgumentException::class.java) {
            loanCalculator.calculate(presentValue, durationInMonths, interestRate)
        }
        assertThat(exception.message).isEqualTo("Present value must be non-negative")
    }
}
