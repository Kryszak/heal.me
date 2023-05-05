package io.github.kryszak.healme.doctor

import arrow.core.Either
import arrow.core.flatMap
import io.github.kryszak.healme.common.TenantStore

class GetDoctorQuery(
    private val doctorStore: DoctorStore,
    private val tenantStore: TenantStore
) {

    fun execute(input: Input): Either<Throwable, Doctor> {
        return tenantStore.getCurrentTenant()
            .flatMap { doctorStore.findDoctor(it, input.doctorId) }
    }

    data class Input(val doctorId: Long)
}