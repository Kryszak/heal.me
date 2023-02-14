package net.kryszak.healme.doctor

import arrow.core.Either
import arrow.core.flatMap
import net.kryszak.healme.common.TenantStore
import net.kryszak.healme.common.exception.DataMismatchException

class UpdateDoctorCommand(
    private val doctorStore: DoctorStore,
    private val tenantStore: TenantStore
) {

    fun execute(input: Input): Either<Throwable, Doctor> =
        Either.catch { validateInput(input) }
            .flatMap { tenantStore.getCurrentTenant() }
            .flatMap { doctorStore.findDoctor(it, input.id) }
            .map { updateDoctor(it, input) }
            .flatMap { doctorStore.updateDoctor(it) }

    private fun validateInput(input: Input) {
        if (input.id != input.resourceId) {
            throw DataMismatchException()
        }
    }

    private fun updateDoctor(doctor: Doctor, input: Input) =
        doctor.copy(
            name = input.name,
            surname = input.surname,
            specialization = input.specialization
        )

    data class Input(
        val resourceId: Long,
        val id: Long,
        val name: String,
        val surname: String,
        val specialization: String,
    )
}