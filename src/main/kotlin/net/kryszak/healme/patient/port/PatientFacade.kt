package net.kryszak.healme.patient.port

import arrow.core.Either
import net.kryszak.healme.patient.GetPatientQuery
import net.kryszak.healme.patient.Patient

class PatientFacade(private val getPatientQuery: GetPatientQuery) {

    fun getPatient(patientId: Long): Either<Throwable, Patient> =
        getPatientQuery.execute(GetPatientQuery.Input(patientId))
}