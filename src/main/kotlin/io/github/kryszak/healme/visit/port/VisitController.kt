package io.github.kryszak.healme.visit.port

import arrow.core.Either
import arrow.core.flatMap
import io.github.kryszak.healme.visit.*
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.data.web.SortDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RestController
class VisitController(
    private val createVisitCommand: CreateVisitCommand,
    private val getVisitsQuery: GetVisitsQuery,
    private val getPatientVisitsQuery: GetPatientVisitsQuery,
    private val updateVisitTimeCommand: UpdateVisitTimeCommand,
    private val deleteVisitCommand: DeleteVisitCommand,
    private val exceptionMapper: VisitExceptionMapper
) {

    private companion object {
        val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
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

    @GetMapping("/visits")
    fun getVisits(
        @PageableDefault(page = 0, size = 20)
        @SortDefault(sort = ["id"], direction = Sort.Direction.ASC)
        pageable: Pageable
    ): ResponseEntity<*> =
        getVisitsQuery.execute(GetVisitsQuery.Input(pageable))
            .map { it.map(VisitDto::from) }
            .fold(exceptionMapper::mapExceptionToResponse) { ResponseEntity.ok(it) }

    @GetMapping("/patients/{patientId}/visits")
    fun getPatientVisits(
        @PageableDefault(page = 0, size = 20)
        @SortDefault(sort = ["id"], direction = Sort.Direction.ASC)
        pageable: Pageable,
        @PathVariable patientId: Long,
    ): ResponseEntity<*> =
        getPatientVisitsQuery.execute(GetPatientVisitsQuery.Input(patientId, pageable))
            .map { it.map(VisitDto::from) }
            .fold(exceptionMapper::mapExceptionToResponse) { ResponseEntity.ok(it) }

    @PatchMapping("/visits/{visitId}")
    fun updateVisitTime(
        @PathVariable visitId: Long,
        @RequestBody dto: UpdateVisitTimeDto
    ): ResponseEntity<*> =
        Either.catch { UpdateVisitTimeCommand.Input(visitId, LocalTime.parse(dto.time, TIME_FORMATTER)) }
            .flatMap(updateVisitTimeCommand::execute)
            .fold(exceptionMapper::mapExceptionToResponse) { ResponseEntity.noContent().build() }

    @DeleteMapping("/visits/{visitId}")
    fun deleteVisit(@PathVariable visitId: Long) =
        deleteVisitCommand.execute(DeleteVisitCommand.Input(visitId))
            .fold(exceptionMapper::mapExceptionToResponse) { ResponseEntity.noContent().build() }
}