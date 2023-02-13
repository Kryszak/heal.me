package net.kryszak.healme.patient

import java.util.UUID
import net.kryszak.healme.authentication.TenantId
import net.kryszak.healme.patient.adapter.PatientEntity

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