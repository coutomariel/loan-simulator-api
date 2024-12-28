package br.com.coutomariel.loansimulator_api.infrastructure.postgres.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Column

@Entity
@Table(name = "interest_rate")
data class InterestRate(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "min_age", nullable = false)
    val minAge: Int,

    @Column(name = "max_age")
    val maxAge: Int? = null,

    @Column(name = "annual_rate", nullable = false)
    val annualRate: Double
)
