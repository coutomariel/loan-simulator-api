package br.com.coutomariel.loansimulator_api.infrastructure.postgres.repository

import br.com.coutomariel.loansimulator_api.infrastructure.postgres.entity.InterestRate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface InterestRateRepository : JpaRepository<InterestRate, Long> {
    @Query("SELECT ir.annualRate FROM InterestRate ir WHERE :age BETWEEN ir.minAge AND COALESCE(ir.maxAge, 999)")
    fun findByAge(@Param("age") age: Int): Double?
}
