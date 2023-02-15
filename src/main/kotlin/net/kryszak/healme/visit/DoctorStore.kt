package net.kryszak.healme.visit

import arrow.core.Either
import net.kryszak.healme.doctor.Doctor

interface DoctorStore {

    fun getDoctor(doctorId: Long): Either<Throwable, Doctor>
}