package io.github.kryszak.healme.visit.adapter

import arrow.core.Either
import io.github.kryszak.healme.doctor.Doctor
import io.github.kryszak.healme.doctor.port.DoctorFacade
import io.github.kryszak.healme.visit.DoctorStore

class LocalDoctorStore(private val doctorFacade: DoctorFacade) : DoctorStore {
    override fun getDoctor(doctorId: Long): Either<Throwable, Doctor> =
        doctorFacade.getDoctor(doctorId)
}