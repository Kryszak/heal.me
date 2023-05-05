package io.github.kryszak.healme.doctor

import io.github.kryszak.healme.authentication.TenantId
import io.github.kryszak.healme.doctor.adapter.DoctorEntity
import java.util.*

const val DOCTOR_ID = 1L
const val DOCTOR_NAME = "Jan"
const val DOCTOR_SURNAME = "Kowalski"
const val DOCTOR_SPECIALIZATION = "Chirurg"
val DOCTOR_OWNER = TenantId(UUID.randomUUID())

fun testDoctor() = Doctor(DOCTOR_ID, DOCTOR_NAME, DOCTOR_SURNAME, DOCTOR_SPECIALIZATION, DOCTOR_OWNER)

fun testDoctorEntity() = DoctorEntity().apply {
    id = DOCTOR_ID
    name = DOCTOR_NAME
    surname = DOCTOR_SURNAME
    specialization = DOCTOR_SPECIALIZATION
    owner = DOCTOR_OWNER.value
}