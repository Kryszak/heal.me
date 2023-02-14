package net.kryszak.healme.doctor.adapter

import arrow.core.Either
import net.kryszak.healme.doctor.CreateDoctorParams
import net.kryszak.healme.doctor.Doctor
import net.kryszak.healme.doctor.DoctorStore

class SqlDoctorStore(
    private val doctorRepository: DoctorRepository
) : DoctorStore {
    override fun saveDoctor(params: CreateDoctorParams): Either<Throwable, Doctor> =
        DoctorEntity.fromParams(params)
            .let { Either.catch { doctorRepository.save(it) } }
            .map(DoctorEntity::toDomain)

}