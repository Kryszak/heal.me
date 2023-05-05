package io.github.kryszak.healme.patient

import io.github.kryszak.healme.authentication.TenantId
import io.github.kryszak.healme.patient.adapter.PatientEntity
import java.util.*

const val PATIENT_ID = 1L
const val PATIENT_NAME = "Jan"
const val PATIENT_SURNAME = "Kowalski"
const val PATIENT_ADDRESS = "Losowa street 2, Randomize Town"
val PATIENT_OWNER = TenantId(UUID.randomUUID())

fun testPatient() = Patient(PATIENT_ID, PATIENT_NAME, PATIENT_SURNAME, PATIENT_ADDRESS, PATIENT_OWNER)

fun testPatientEntity() = PatientEntity().apply {
    id = PATIENT_ID
    name = PATIENT_NAME
    surname = PATIENT_SURNAME
    address = PATIENT_ADDRESS
    owner = PATIENT_OWNER.value
}