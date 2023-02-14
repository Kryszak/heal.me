package net.kryszak.healme.doctor.port

import net.kryszak.healme.common.exception.ExceptionMapper
import net.kryszak.healme.doctor.CreateDoctorCommand
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/doctors")
class DoctorController(
    private val createDoctorCommand: CreateDoctorCommand,
    private val exceptionMapper: ExceptionMapper,
) {

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