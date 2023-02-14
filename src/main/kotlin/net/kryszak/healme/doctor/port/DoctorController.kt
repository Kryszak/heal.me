package net.kryszak.healme.doctor.port

import net.kryszak.healme.common.exception.ExceptionMapper
import net.kryszak.healme.doctor.CreateDoctorCommand
import net.kryszak.healme.doctor.GetDoctorsQuery
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.data.web.SortDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/doctors")
class DoctorController(
    private val createDoctorCommand: CreateDoctorCommand,
    private val getDoctorsQuery: GetDoctorsQuery,
    private val exceptionMapper: ExceptionMapper,
) {

    @GetMapping
    fun getDoctors(
        @PageableDefault(page = 0, size = 20)
        @SortDefault(sort = ["id"], direction = Sort.Direction.ASC)
        pageable: Pageable
    ): ResponseEntity<*> =
        getDoctorsQuery.execute(GetDoctorsQuery.Input(pageable))
            .map { it.map(DoctorDto::from) }
            .fold(exceptionMapper::mapExceptionToDto) { ResponseEntity.ok(it) }

    @PostMapping
    fun createDoctor(@RequestBody dto: CreateDoctorDto) =
        createDoctorCommand.execute(
            CreateDoctorCommand.Input(
                dto.name,
                dto.surname,
                dto.specialization
            )
        ).fold(exceptionMapper::mapExceptionToDto) { ResponseEntity.status(HttpStatus.CREATED).build() }

}