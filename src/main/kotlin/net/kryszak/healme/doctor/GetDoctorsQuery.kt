package net.kryszak.healme.doctor

import arrow.core.Either
import arrow.core.flatMap
import net.kryszak.healme.common.TenantStore
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class GetDoctorsQuery(
    private val doctorStore: DoctorStore,
    private val tenantStore: TenantStore
) {

    fun execute(input: Input): Either<Throwable, Page<Doctor>> =
        tenantStore.getCurrentTenant()
            .flatMap { doctorStore.findDoctors(it, input.pageable) }

    data class Input(val pageable: Pageable)
}