package net.kryszak.healme.visit.port

data class CreateVisitDto(
    val date: String,
    val time: String,
    val place: String,
    val doctorId: Long,
    val patientId: Long,
)
