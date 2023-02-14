package net.kryszak.healme.doctor

import arrow.core.Either
import net.kryszak.healme.authentication.TenantId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface DoctorStore {

    fun saveDoctor(params: CreateDoctorParams): Either<Throwable, Doctor>

    fun findDoctors(tenantId: TenantId, pageable: Pageable): Either<Throwable, Page<Doctor>>
}

data class CreateDoctorParams(
    val name: String,
    val surname: String,
    val specialization: String,
    val owner: TenantId,
)