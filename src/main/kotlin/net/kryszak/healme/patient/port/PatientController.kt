package net.kryszak.healme.patient.port

import net.kryszak.healme.common.exception.ExceptionMapper
import net.kryszak.healme.patient.CreatePatientCommand
import net.kryszak.healme.patient.GetPatientsQuery
import net.kryszak.healme.patient.GetPatientsQuery.Input
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.data.web.SortDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/patients")
class PatientController(
    val createPatientCommand: CreatePatientCommand,
    val getPatientsQuery: GetPatientsQuery,
    val exceptionMapper: ExceptionMapper
) {

    @GetMapping
    fun getPatients(
        @PageableDefault(page = 0, size = 20)
        @SortDefault(sort = ["id"], direction = Sort.Direction.ASC)
        pageable: Pageable
    ): ResponseEntity<*> =
        getPatientsQuery.execute(Input(pageable))
            .fold(exceptionMapper::mapExceptionToDto) { ResponseEntity.ok(it.map(PatientDto::from)) }


    @PostMapping
    fun createPatient(@RequestBody dto: CreatePatientDto): ResponseEntity<*> =
        createPatientCommand.execute(
            CreatePatientCommand.Input(
                dto.name,
                dto.surname,
                dto.address
            )
        ).fold(exceptionMapper::mapExceptionToDto) { ResponseEntity.status(HttpStatus.CREATED).build() }

}