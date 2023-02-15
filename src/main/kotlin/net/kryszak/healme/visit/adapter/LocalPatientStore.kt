package net.kryszak.healme.visit.adapter

import arrow.core.Either
import net.kryszak.healme.patient.Patient
import net.kryszak.healme.patient.port.PatientFacade
import net.kryszak.healme.visit.PatientStore

class LocalPatientStore(private val patientFacade: PatientFacade) : PatientStore {
    override fun getPatient(patientId: Long): Either<Throwable, Patient> =
        patientFacade.getPatient(patientId)
}