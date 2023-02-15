package net.kryszak.healme.visit.port

import java.time.format.DateTimeParseException
import net.kryszak.healme.common.exception.ExceptionMapper
import net.kryszak.healme.visit.VisitTimeAlreadyTakenException
import org.springframework.http.ResponseEntity

class VisitExceptionMapper(private val exceptionMapper: ExceptionMapper) {

    fun mapExceptionToResponse(exception: Throwable): ResponseEntity<Any> =
        when (exception) {
            is VisitTimeAlreadyTakenException -> ResponseEntity.badRequest().body(exception.message)
            is DateTimeParseException -> ResponseEntity.badRequest()
                .body("Wrong format used for either date (should be: yyyy-mm-dd) or time(should be: HH:mm).")

            else -> exceptionMapper.mapExceptionToResponse(exception)
        }
}