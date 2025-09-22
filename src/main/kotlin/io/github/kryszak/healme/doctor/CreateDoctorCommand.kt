package io.github.kryszak.healme.doctor

import arrow.core.Either
import arrow.core.flatMap
import io.github.kryszak.healme.common.TenantStore

class CreateDoctorCommand(
    private val doctorStore: DoctorStore,
    private val tenantStore: TenantStore
) {

    fun execute(input: Input): Either<Throwable, Long> {
        return tenantStore.getCurrentTenant()
            .flatMap {
                doctorStore.saveDoctor(
                    CreateDoctorParams(
                        input.name,
                        input.surname, input.specialization, it
                    )
                )
            }.map { it.id }
    }

    data class Input(
        val name: String,
        val surname: String,
        val specialization: String,
    )
}