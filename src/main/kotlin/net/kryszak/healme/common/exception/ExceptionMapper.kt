package net.kryszak.healme.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException

class ExceptionMapper {

    fun mapExceptionToDto(exception: Throwable): ResponseEntity<Any> {
        return when (exception) {
            is BadCredentialsException -> ResponseEntity.status(HttpStatus.FORBIDDEN).build()
            is DataNotFoundException -> ResponseEntity.notFound().build()
            is DataMismatchException -> ResponseEntity.badRequest().build()
            else -> ResponseEntity.internalServerError().build()
        }
    }
}