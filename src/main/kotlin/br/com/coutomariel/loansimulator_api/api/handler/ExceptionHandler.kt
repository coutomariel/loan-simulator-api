package br.com.coutomariel.loansimulator_api.api.handler

import br.com.coutomariel.loansimulator_api.api.handler.response.ErrorDetailsResponse
import br.com.coutomariel.loansimulator_api.api.handler.response.ErrorField
import br.com.coutomariel.loansimulator_api.domain.exception.BirthDateNotValidException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        exception: MethodArgumentNotValidException,
        request: WebRequest,
    ): ResponseEntity<ErrorDetailsResponse> {
        val errorDetails = ErrorDetailsResponse(
            httpCode = HttpStatus.BAD_REQUEST.value(),
            errorFields = exception.bindingResult.fieldErrors.map { error ->
                ErrorField(
                    message = error.defaultMessage ?: "Invalid",
                    field = error.field
                )
            }
        )
        return ResponseEntity.badRequest().body(errorDetails)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        exception: IllegalArgumentException,
        request: WebRequest,
    ): ResponseEntity<ErrorDetailsResponse> {
        val errorDetails = ErrorDetailsResponse(
            httpCode = HttpStatus.UNPROCESSABLE_ENTITY.value(),
            errorFields = null
        )
        return ResponseEntity.unprocessableEntity().body(errorDetails)
    }

    @ExceptionHandler(BirthDateNotValidException::class)
    fun handleBirthDateNotValidException(
        exception: BirthDateNotValidException,
        request: WebRequest,
    ): ResponseEntity<ErrorDetailsResponse> {
        val errorDetails = ErrorDetailsResponse(
            httpCode = HttpStatus.UNPROCESSABLE_ENTITY.value(),
            errorFields = listOf(ErrorField(exception.message, "birthDate"))
        )
        return ResponseEntity.unprocessableEntity().body(errorDetails)
    }
}
