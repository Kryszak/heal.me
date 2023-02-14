package net.kryszak.healme.doctor

import arrow.core.Either
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import net.kryszak.healme.common.TenantStore
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class GetDoctorsQueryTest : ShouldSpec({
    val doctorStore = mockk<DoctorStore>()
    val tenantStore = mockk<TenantStore>()
    val query = GetDoctorsQuery(doctorStore, tenantStore)

    should("retrieve doctors list") {
        //given
        val pageable = Pageable.unpaged()
        every { tenantStore.getCurrentTenant() } returns Either.Right(DOCTOR_OWNER)
        val pagedPatients = PageImpl(listOf(testDoctor()))
        every {
            doctorStore.findDoctors(
                DOCTOR_OWNER,
                pageable
            )
        } returns Either.Right(pagedPatients)

        //when
        val result = query.execute(GetDoctorsQuery.Input(pageable))

        //then
        result shouldBeRight pagedPatients
    }

    should("return error when fetching doctors fails") {
        //given
        val pageable = Pageable.unpaged()
        val exception = Exception()
        every { tenantStore.getCurrentTenant() } returns Either.Right(DOCTOR_OWNER)
        every {
            doctorStore.findDoctors(
                DOCTOR_OWNER,
                pageable
            )
        } returns Either.Left(exception)

        //when
        val result = query.execute(GetDoctorsQuery.Input(pageable))

        //then
        result shouldBeLeft exception
    }

    should("return error when tenant id is not found") {
        //given
        val pageable = Pageable.unpaged()
        val exception = Exception()
        every { tenantStore.getCurrentTenant() } returns Either.Left(exception)

        //when
        val result = query.execute(GetDoctorsQuery.Input(pageable))

        //then
        result shouldBeLeft exception
    }
})