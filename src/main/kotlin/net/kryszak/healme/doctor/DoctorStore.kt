package net.kryszak.healme.doctor

import arrow.core.Either
import net.kryszak.healme.authentication.TenantId

interface DoctorStore {

    fun saveDoctor(params: CreateDoctorParams): Either<Throwable, Doctor>
}

data class CreateDoctorParams(
    val name: String,
    val surname: String,
    val specialization: String,
    val owner: TenantId,
)