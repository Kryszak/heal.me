package net.kryszak.healme.doctor

import arrow.core.Either
import arrow.core.flatMap
import net.kryszak.healme.common.TenantStore

class DeleteDoctorCommand(
    private val doctorStore: DoctorStore,
    private val tenantStore: TenantStore
) {

    fun execute(input: Input): Either<Throwable, Unit> {
        return tenantStore.getCurrentTenant()
            .flatMap { doctorStore.findDoctor(it, input.doctorId) }
            .flatMap { doctorStore.deleteDoctor(it) }
    }

    data class Input(val doctorId: Long)
}