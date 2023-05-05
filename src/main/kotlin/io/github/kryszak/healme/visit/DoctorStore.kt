package io.github.kryszak.healme.visit

import arrow.core.Either
import io.github.kryszak.healme.doctor.Doctor

interface DoctorStore {

    fun getDoctor(doctorId: Long): Either<Throwable, Doctor>
}