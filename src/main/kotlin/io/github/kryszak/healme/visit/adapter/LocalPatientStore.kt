package io.github.kryszak.healme.visit.adapter

import arrow.core.Either
import io.github.kryszak.healme.patient.Patient
import io.github.kryszak.healme.patient.port.PatientFacade
import io.github.kryszak.healme.visit.PatientStore

class LocalPatientStore(private val patientFacade: PatientFacade) : PatientStore {
    override fun getPatient(patientId: Long): Either<Throwable, Patient> =
        patientFacade.getPatient(patientId)
}