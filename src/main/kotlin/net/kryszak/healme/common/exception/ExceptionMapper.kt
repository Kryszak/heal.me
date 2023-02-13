package net.kryszak.healme.common.exception

import net.kryszak.healme.common.model.ErrorResponse
import org.springframework.http.ResponseEntity

class ExceptionMapper {

    fun mapExceptionToDto(exception: Throwable): ResponseEntity<ErrorResponse> {
        return when (exception) {
            is DataNotFoundException -> ResponseEntity.notFound().build()
            else -> ResponseEntity.internalServerError().build()
        }
    }
}