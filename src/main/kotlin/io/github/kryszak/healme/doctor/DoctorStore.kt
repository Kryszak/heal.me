package io.github.kryszak.healme.doctor

import arrow.core.Either
import io.github.kryszak.healme.authentication.TenantId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface DoctorStore {

    fun saveDoctor(params: CreateDoctorParams): Either<Throwable, Doctor>

    fun findDoctors(
        tenantId: TenantId,
        pageable: Pageable
    ): Either<Throwable, Page<Doctor>>

    fun findDoctor(
        tenantId: TenantId,
        doctorId: Long
    ): Either<Throwable, Doctor>

    fun updateDoctor(doctor: Doctor): Either<Throwable, Doctor>

    fun deleteDoctor(doctor: Doctor): Either<Throwable, Unit>
}

data class CreateDoctorParams(
    val name: String,
    val surname: String,
    val specialization: String,
    val owner: TenantId,
)