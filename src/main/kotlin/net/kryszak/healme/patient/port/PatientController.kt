package net.kryszak.healme.patient.port

import net.kryszak.healme.common.exception.ExceptionMapper
import net.kryszak.healme.patient.CreatePatientCommand
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/patients")
class PatientController(
    val createPatientCommand: CreatePatientCommand,
    val exceptionMapper: ExceptionMapper
) {

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