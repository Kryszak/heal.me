package net.kryszak.healme.doctor.port

import net.kryszak.healme.doctor.Doctor

data class DoctorDto(
    val id: Long,
    val name: String,
    val surname: String,
    val specialization: String,
) {
    companion object {
        fun from(doctor: Doctor) =
            DoctorDto(
                doctor.id,
                doctor.name,
                doctor.surname,
                doctor.specialization
            )
    }
}
