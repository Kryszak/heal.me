package net.kryszak.healme.doctor.port

import arrow.core.Either
import net.kryszak.healme.doctor.Doctor
import net.kryszak.healme.doctor.GetDoctorQuery

class DoctorFacade(private val getDoctorQuery: GetDoctorQuery) {

    fun getDoctor(doctorId: Long): Either<Throwable, Doctor> =
        getDoctorQuery.execute(GetDoctorQuery.Input(doctorId))
}