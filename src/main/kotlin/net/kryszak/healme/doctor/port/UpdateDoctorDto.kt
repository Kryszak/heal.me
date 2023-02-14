package net.kryszak.healme.doctor.port

data class UpdateDoctorDto(
    val id: Long,
    val name: String,
    val surname: String,
    val specialization: String,
)
