package net.kryszak.healme.doctor.port

data class CreateDoctorDto(
    val name: String,
    val surname: String,
    val specialization: String,
)
