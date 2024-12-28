package br.com.coutomariel.loansimulator_api.domain.service.loan

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

@Component
class LoanCalculator {

    private val logger = LoggerFactory.getLogger(LoanCalculator::class.java)

    fun calculate(
        presentValue: BigDecimal,
        durationInMonths: Int,
        interestRate: BigDecimal
    ): BigDecimal {
        logger.info("Calculating monthly installment:pv-($presentValue) dur-($durationInMonths) ir-($interestRate)")
        if (presentValue < BigDecimal.ZERO) {
            logger.error("Present value negative:($presentValue)")
            throw IllegalArgumentException("Present value must be non-negative")
        }

        if (interestRate == BigDecimal.ZERO) {
            logger.info("Calculating monthly installment with interest rate zero for age")
            return presentValue.divide(durationInMonths.toBigDecimal(), 10, RoundingMode.HALF_EVEN)
        }

        val mathContext = MathContext(10, RoundingMode.HALF_EVEN)
        val monthlyInterestRate = interestRate.divide(BigDecimal(12), mathContext)

        val numerator = calculateNumerator(presentValue, monthlyInterestRate, mathContext)
        val denominator = calculateDenominator(monthlyInterestRate, durationInMonths, mathContext)
        return numerator.divide(denominator, 10, RoundingMode.HALF_EVEN)
    }

    private fun calculateNumerator(
        presentValue: BigDecimal,
        monthlyInterestRate: BigDecimal,
        mathContext: MathContext
    ): BigDecimal {
        return presentValue.multiply(monthlyInterestRate, mathContext)
    }

    private fun calculateDenominator(
        monthlyInterestRate: BigDecimal,
        durationInMonths: Int,
        mathContext: MathContext
    ): BigDecimal {
        val onePlusInterest = BigDecimal.ONE.add(monthlyInterestRate, mathContext)
        val negativeDuration = -durationInMonths
        val power = onePlusInterest.pow(negativeDuration, mathContext)
        return BigDecimal.ONE.subtract(power, mathContext)
    }
}
