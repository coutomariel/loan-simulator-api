package br.com.coutomariel.loansimulator_api.domain.service.loan

import br.com.coutomariel.loansimulator_api.domain.exception.BirthDateNotValidException
import br.com.coutomariel.loansimulator_api.domain.exception.InterestRateNotFoundException
import br.com.coutomariel.loansimulator_api.domain.model.LoanSimulation
import br.com.coutomariel.loansimulator_api.domain.model.LoanSimulationResult
import br.com.coutomariel.loansimulator_api.infrastructure.postgres.repository.InterestRateRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period

@Service
class LoanSimulationService(
    val loanCalculator: LoanCalculator,
    val interestRateRepository: InterestRateRepository
) {

    private val logger = LoggerFactory.getLogger(LoanSimulationService::class.java)

    fun simulate(loanSimulation: LoanSimulation): LoanSimulationResult {
        logger.info("Simulatin loan:($loanSimulation)")
        if (loanSimulation.dateBirth.isAfter(LocalDate.now())) {
            logger.error("Birth date can't greater than today: (${loanSimulation.dateBirth})")
            throw BirthDateNotValidException("Birth date can't greater than today")
        }
        val age = Period.between(loanSimulation.dateBirth, LocalDate.now()).years
        val interestRate = getInterestRate(age)

        val monthlyInstallment = loanCalculator.calculate(
            loanSimulation.value, loanSimulation.durationInMonths, interestRate
        )

        val totalValue = monthlyInstallment.multiply(loanSimulation.durationInMonths.toBigDecimal())

        return LoanSimulationResult(
            totalValue = totalValue,
            interestRate = interestRate,
            monthlyInstallment = monthlyInstallment
        )
    }

    private fun getInterestRate(age: Int): BigDecimal {
        val interestRate = interestRateRepository.findByAge(age)?.toBigDecimal()
            ?: run {
                logger.error("Interest rate not found for age: $age")
                throw InterestRateNotFoundException("Interest rate not found.")
            }
        return interestRate
    }
}
