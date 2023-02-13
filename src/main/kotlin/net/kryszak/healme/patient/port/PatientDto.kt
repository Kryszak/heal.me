package net.kryszak.healme.patient.port

import net.kryszak.healme.patient.Patient

data class PatientDto(
    val id: Long,
    val name: String,
    val surname: String,
    val address: String,
) {
    companion object {
        fun from(patient: Patient) =
            PatientDto(
                patient.id,
                patient.name,
                patient.surname,
                patient.address
            )
    }
}