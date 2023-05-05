package io.github.kryszak.healme.patient.port

data class CreatePatientDto(
    val name: String,
    val surname: String,
    val address: String,
)