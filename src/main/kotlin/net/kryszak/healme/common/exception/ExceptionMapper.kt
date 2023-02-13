package net.kryszak.healme.common.exception

import net.kryszak.healme.common.model.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException

class ExceptionMapper {

    fun mapExceptionToDto(exception: Throwable): ResponseEntity<ErrorResponse> {
        return when (exception) {
            is BadCredentialsException -> ResponseEntity.status(HttpStatus.FORBIDDEN).build()
            is DataNotFoundException -> ResponseEntity.notFound().build()
            else -> ResponseEntity.internalServerError().build()
        }
    }
}