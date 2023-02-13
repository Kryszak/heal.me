package net.kryszak.healme.patient.adapter

import arrow.core.Either
import net.kryszak.healme.patient.CreatePatientParams
import net.kryszak.healme.patient.Patient
import net.kryszak.healme.patient.PatientStore

class SqlPatientStore(private val patientRepository: PatientRepository) : PatientStore {

    override fun savePatient(params: CreatePatientParams): Either<Throwable, Patient> =
        PatientEntity.from(params)
            .let { Either.catch { patientRepository.save(it) } }
            .map(PatientEntity::toDomain)
}