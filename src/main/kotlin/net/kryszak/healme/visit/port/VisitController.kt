package net.kryszak.healme.visit.port

import arrow.core.Either
import arrow.core.flatMap
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import net.kryszak.healme.visit.CreateVisitCommand
import net.kryszak.healme.visit.DeleteVisitCommand
import net.kryszak.healme.visit.GetVisitsQuery
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.data.web.SortDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class VisitController(
    private val createVisitCommand: CreateVisitCommand,
    private val getVisitsQuery: GetVisitsQuery,
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

    @GetMapping("/visits")
    fun getVisits(
        @PageableDefault(page = 0, size = 20)
        @SortDefault(sort = ["id"], direction = Sort.Direction.ASC)
        pageable: Pageable
    ): ResponseEntity<*> =
        getVisitsQuery.execute(GetVisitsQuery.Input(pageable))
            .map { it.map(VisitDto::from) }
            .fold(exceptionMapper::mapExceptionToResponse) { ResponseEntity.ok(it) }

    @DeleteMapping("/visits/{visitId}")
    fun deleteVisit(@PathVariable visitId: Long) =
        deleteVisitCommand.execute(DeleteVisitCommand.Input(visitId))
            .fold(exceptionMapper::mapExceptionToResponse) { ResponseEntity.noContent().build() }
}