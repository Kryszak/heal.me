package net.kryszak.healme.patient.port

import net.kryszak.healme.common.exception.ExceptionMapper
import net.kryszak.healme.patient.*
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
    private val createPatientCommand: CreatePatientCommand,
    private val getPatientsQuery: GetPatientsQuery,
    private val getPatientQuery: GetPatientQuery,
    private val updatePatientCommand: UpdatePatientCommand,
    private val deletePatientCommand: DeletePatientCommand,
    private val exceptionMapper: ExceptionMapper
) {

    @GetMapping
    fun getPatients(
        @PageableDefault(page = 0, size = 20)
        @SortDefault(sort = ["id"], direction = Sort.Direction.ASC)
        pageable: Pageable
    ): ResponseEntity<*> =
        getPatientsQuery.execute(Input(pageable))
            .map { it.map(PatientDto::from) }
            .fold(exceptionMapper::mapExceptionToDto) { ResponseEntity.ok(it) }

    @GetMapping("/{patientId}")
    fun getPatient(@PathVariable patientId: Long): ResponseEntity<*> =
        getPatientQuery.execute(GetPatientQuery.Input(patientId))
            .map(PatientDto::from)
            .fold(exceptionMapper::mapExceptionToDto) { ResponseEntity.ok(it) }

    @PostMapping
    fun createPatient(@RequestBody dto: CreatePatientDto): ResponseEntity<*> =
        createPatientCommand.execute(
            CreatePatientCommand.Input(
                dto.name,
                dto.surname,
                dto.address
            )
        ).fold(exceptionMapper::mapExceptionToDto) { ResponseEntity.status(HttpStatus.CREATED).build() }

    @PutMapping("/{patientId}")
    fun updatePatient(@PathVariable patientId: Long, @RequestBody dto: UpdatePatientDto): ResponseEntity<*> =
        updatePatientCommand.execute(
            UpdatePatientCommand.Input(
                patientId,
                dto.id,
                dto.name,
                dto.surname,
                dto.address,
            )
        ).fold(exceptionMapper::mapExceptionToDto) { ResponseEntity.ok(it) }

    @DeleteMapping("/{patientId}")
    fun deletePatient(@PathVariable patientId: Long): ResponseEntity<*> =
        deletePatientCommand.execute(DeletePatientCommand.Input(patientId))
            .fold(exceptionMapper::mapExceptionToDto) { ResponseEntity.noContent().build() }
}