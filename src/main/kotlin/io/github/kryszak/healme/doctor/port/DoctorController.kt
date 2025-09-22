package io.github.kryszak.healme.doctor.port

import io.github.kryszak.healme.common.exception.ExceptionMapper
import io.github.kryszak.healme.doctor.*
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.data.web.SortDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/doctors")
class DoctorController(
    private val createDoctorCommand: CreateDoctorCommand,
    private val getDoctorsQuery: GetDoctorsQuery,
    private val getDoctorQuery: GetDoctorQuery,
    private val updateDoctorCommand: UpdateDoctorCommand,
    private val deleteDoctorCommand: DeleteDoctorCommand,
    private val exceptionMapper: ExceptionMapper,
) {

    @GetMapping
    fun getDoctors(
        @PageableDefault(page = 0, size = 20)
        @SortDefault(sort = ["id"], direction = Sort.Direction.ASC)
        pageable: Pageable,
    ): ResponseEntity<*> =
        getDoctorsQuery.execute(GetDoctorsQuery.Input(pageable))
            .map { it.map(DoctorDto::from) }
            .fold(exceptionMapper::mapExceptionToResponse) { ResponseEntity.ok(it) }

    @GetMapping("/{doctorId}")
    fun getDoctor(@PathVariable doctorId: Long): ResponseEntity<*> =
        getDoctorQuery.execute(GetDoctorQuery.Input(doctorId))
            .map(DoctorDto::from)
            .fold(exceptionMapper::mapExceptionToResponse) { ResponseEntity.ok(it) }

    @PostMapping
    fun createDoctor(@RequestBody dto: CreateDoctorDto) =
        createDoctorCommand.execute(
            CreateDoctorCommand.Input(
                dto.name,
                dto.surname,
                dto.specialization
            )
        ).fold(exceptionMapper::mapExceptionToResponse) {
            ResponseEntity.created(URI.create("/doctors/${it}")).build()
        }

    @PutMapping("/{doctorId}")
    fun updateDoctor(@PathVariable doctorId: Long, @RequestBody dto: UpdateDoctorDto): ResponseEntity<*> =
        updateDoctorCommand.execute(
            UpdateDoctorCommand.Input(
                doctorId,
                dto.id,
                dto.name,
                dto.surname,
                dto.specialization,
            )
        ).fold(exceptionMapper::mapExceptionToResponse) { ResponseEntity.ok(it) }

    @DeleteMapping("/{doctorId}")
    fun deleteDoctor(@PathVariable doctorId: Long): ResponseEntity<*> =
        deleteDoctorCommand.execute(DeleteDoctorCommand.Input(doctorId))
            .fold(exceptionMapper::mapExceptionToResponse) { ResponseEntity.noContent().build() }
}