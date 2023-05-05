package io.github.kryszak.healme.doctor.port

import arrow.core.Either
import io.github.kryszak.healme.doctor.Doctor
import io.github.kryszak.healme.doctor.GetDoctorQuery

class DoctorFacade(private val getDoctorQuery: GetDoctorQuery) {

    fun getDoctor(doctorId: Long): Either<Throwable, Doctor> =
        getDoctorQuery.execute(GetDoctorQuery.Input(doctorId))
}