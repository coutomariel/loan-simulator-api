package br.com.coutomariel.loansimulator_api.api.validation

import br.com.coutomariel.loansimulator_api.utils.DateProperties.DATE_FORMATTER
import br.com.coutomariel.loansimulator_api.utils.DateProperties.DATE_FORMAT_PATTERN
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import java.time.LocalDate
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidDateValidator::class])
@MustBeDocumented
annotation class ValidDate(
    val message: String = "Date format invalid. Use format:($DATE_FORMAT_PATTERN)",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)

class ValidDateValidator : ConstraintValidator<ValidDate, String> {

    override fun initialize(constraintAnnotation: ValidDate) {}

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) return true
        return runCatching { LocalDate.parse(value, DATE_FORMATTER) }.isSuccess
    }
}
