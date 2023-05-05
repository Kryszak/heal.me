package io.github.kryszak.healme.patient.port

import arrow.core.Either
import io.github.kryszak.healme.patient.GetPatientQuery
import io.github.kryszak.healme.patient.Patient

class PatientFacade(private val getPatientQuery: GetPatientQuery) {

    fun getPatient(patientId: Long): Either<Throwable, Patient> =
        getPatientQuery.execute(GetPatientQuery.Input(patientId))
}