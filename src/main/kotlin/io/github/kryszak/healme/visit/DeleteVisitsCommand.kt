package io.github.kryszak.healme.visit

import arrow.core.Either
import arrow.core.flatMap
import io.github.kryszak.healme.common.TenantStore

class DeleteVisitsCommand(
    private val visitStore: VisitStore,
    private val tenantStore: TenantStore,
) {

    fun execute(input: Input): Either<Throwable, Unit> =
        tenantStore.getCurrentTenant()
            .flatMap {
                when (input.deleteBy) {
                    DeleteBy.DOCTOR -> visitStore.deleteByDoctor(input.id, it)
                    DeleteBy.PATIENT -> visitStore.deleteByPatient(input.id, it)
                }
            }

    data class Input(
        val id: Long,
        val deleteBy: DeleteBy
    )

    enum class DeleteBy {
        DOCTOR, PATIENT
    }
}