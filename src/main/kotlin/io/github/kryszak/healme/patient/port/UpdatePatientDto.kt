package io.github.kryszak.healme.patient.port

data class UpdatePatientDto(
    val id: Long,
    val name: String,
    val surname: String,
    val address: String,
)