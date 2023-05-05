package io.github.kryszak.healme.visit

import io.github.kryszak.healme.authentication.TenantId
import io.github.kryszak.healme.doctor.testDoctor
import io.github.kryszak.healme.doctor.testDoctorEntity
import io.github.kryszak.healme.patient.testPatient
import io.github.kryszak.healme.patient.testPatientEntity
import io.github.kryszak.healme.visit.adapter.VisitEntity
import java.time.LocalDateTime
import java.util.*

const val VISIT_ID = 1L
val VISIT_DATE_TIME: LocalDateTime = LocalDateTime.now()
const val VISIT_PLACE = "Visit place"
val VISIT_OWNER = TenantId(UUID.randomUUID())

fun testVisit() = Visit(VISIT_ID, VISIT_DATE_TIME, VISIT_PLACE, testDoctor(), testPatient(), VISIT_OWNER)

fun testVisitEntity() = VisitEntity().apply {
    id = VISIT_ID
    dateTime = VISIT_DATE_TIME
    place = VISIT_PLACE
    doctor = testDoctorEntity()
    patient = testPatientEntity()
    owner = VISIT_OWNER.value
}