package com.example.betgame.utils

import jakarta.persistence.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import com.example.betgame.data.ExceptionDTO
import com.example.betgame.data.MaxBetAmountExceededEx

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleEntityNotFoundException(ex: EntityNotFoundException): ExceptionDTO = ExceptionDTO("ENF_01", ex.message)

    @ExceptionHandler(BadCredentialsException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadCredentialsException(ex: BadCredentialsException): ExceptionDTO = ExceptionDTO("BC_01", ex.message)

    @ExceptionHandler(MaxBetAmountExceededEx::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMaxBetAmountExceededEx(ex: MaxBetAmountExceededEx): ExceptionDTO = ExceptionDTO(ex.errorCode, ex.errorMessage)

    @ExceptionHandler(RuntimeException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleRuntimeException(ex: RuntimeException): ExceptionDTO = ExceptionDTO("ISE_01", ex.message)

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(ex: Exception): ExceptionDTO = ExceptionDTO("ISE_02", ex.message)

    @ExceptionHandler(AccessDeniedException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleAccessDeniedException(ex: AccessDeniedException): ExceptionDTO = ExceptionDTO("NOA_01", ex.message)

    @ExceptionHandler(AuthenticationException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleAuthenticationException(ex: AuthenticationException): ExceptionDTO = ExceptionDTO("AUT_01", ex.message)

    @ExceptionHandler(io.jsonwebtoken.ExpiredJwtException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleExpiredJwtException(ex: io.jsonwebtoken.ExpiredJwtException): ExceptionDTO = ExceptionDTO("AUT_02", ex.message)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ExceptionDTO {
        val errors = ex.bindingResult.fieldErrors.map { "${it.field}: ${it.defaultMessage}" }
        return ExceptionDTO("VAL_01", "Validation failed").apply {
            errorDetails.addAll(errors)
        }
    }

}
