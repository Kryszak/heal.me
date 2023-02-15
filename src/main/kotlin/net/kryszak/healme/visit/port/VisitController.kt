package net.kryszak.healme.visit.port

import arrow.core.Either
import arrow.core.flatMap
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import net.kryszak.healme.visit.CreateVisitCommand
import net.kryszak.healme.visit.DeleteVisitCommand
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class VisitController(
    private val createVisitCommand: CreateVisitCommand,
    private val deleteVisitCommand: DeleteVisitCommand,
    private val exceptionMapper: VisitExceptionMapper
) {

    private companion object {
        val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    }

    @PostMapping("/visits")
    fun createVisit(@RequestBody dto: CreateVisitDto): ResponseEntity<*> =
        Either.catch {
            CreateVisitCommand.Input(
                dto.doctorId,
                dto.patientId,
                LocalDateTime.parse("${dto.date} ${dto.time}", DATE_TIME_FORMATTER),
                dto.place
            )
        }
            .flatMap(createVisitCommand::execute)
            .fold(exceptionMapper::mapExceptionToResponse) { ResponseEntity.status(HttpStatus.CREATED).build() }

    @DeleteMapping("/visits/{visitId}")
    fun deleteVisit(@PathVariable visitId: Long) =
        deleteVisitCommand.execute(DeleteVisitCommand.Input(visitId))
            .fold(exceptionMapper::mapExceptionToResponse) { ResponseEntity.noContent().build() }
}