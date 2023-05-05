package io.github.kryszak.healme.visit

import arrow.core.Either
import io.github.kryszak.healme.patient.Patient

interface PatientStore {

    fun getPatient(patientId: Long): Either<Throwable, Patient>
}