package net.kryszak.healme.visit

import arrow.core.Either
import net.kryszak.healme.patient.Patient

interface PatientStore {

    fun getPatient(patientId: Long): Either<Throwable, Patient>
}