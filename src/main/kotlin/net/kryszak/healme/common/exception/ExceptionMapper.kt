package net.kryszak.healme.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException

class ExceptionMapper {

    fun mapExceptionToResponse(exception: Throwable): ResponseEntity<Any> {
        return when (exception) {
            is BadCredentialsException -> ResponseEntity.status(HttpStatus.FORBIDDEN).build()
            is DataNotFoundException -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.message)
            is DataMismatchException -> ResponseEntity.badRequest().build()
            else -> ResponseEntity.internalServerError().build()
        }
    }
}