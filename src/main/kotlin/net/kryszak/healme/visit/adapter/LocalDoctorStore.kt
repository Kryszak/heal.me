package net.kryszak.healme.visit.adapter

import arrow.core.Either
import net.kryszak.healme.doctor.Doctor
import net.kryszak.healme.doctor.port.DoctorFacade
import net.kryszak.healme.visit.DoctorStore

class LocalDoctorStore(private val doctorFacade: DoctorFacade) : DoctorStore {
    override fun getDoctor(doctorId: Long): Either<Throwable, Doctor> =
        doctorFacade.getDoctor(doctorId)
}