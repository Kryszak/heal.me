package net.kryszak.healme.visit

import java.time.LocalDateTime
import java.util.UUID
import net.kryszak.healme.authentication.TenantId
import net.kryszak.healme.doctor.testDoctor
import net.kryszak.healme.doctor.testDoctorEntity
import net.kryszak.healme.patient.testPatient
import net.kryszak.healme.patient.testPatientEntity
import net.kryszak.healme.visit.adapter.VisitEntity

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