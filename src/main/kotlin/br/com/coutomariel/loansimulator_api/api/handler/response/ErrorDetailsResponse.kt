package br.com.coutomariel.loansimulator_api.api.handler.response

data class ErrorDetailsResponse(
    val httpCode: Int,
    var errorFields: List<ErrorField>?
)
