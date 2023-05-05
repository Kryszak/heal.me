package io.github.kryszak.healme.visit

import arrow.core.Either
import io.github.kryszak.healme.common.TenantStore
import io.github.kryszak.healme.patient.PATIENT_ID
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class GetPatientVisitsQueryTest : ShouldSpec({
    val visitStore = mockk<VisitStore>()
    val tenantStore = mockk<TenantStore>()
    val query = GetPatientVisitsQuery(visitStore, tenantStore)

    should("retrieve patient visits list") {
        //given
        val pageable = Pageable.unpaged()
        every { tenantStore.getCurrentTenant() } returns Either.Right(VISIT_OWNER)
        val pagedVisits = PageImpl(listOf(testVisit()))
        every {
            visitStore.findPatientVisits(
                PATIENT_ID,
                VISIT_OWNER,
                pageable
            )
        } returns Either.Right(pagedVisits)

        //when
        val result = query.execute(GetPatientVisitsQuery.Input(PATIENT_ID, pageable))

        //then
        result shouldBeRight pagedVisits
    }

    should("return error when fetching patient visits fails") {
        //given
        val pageable = Pageable.unpaged()
        val exception = Exception()
        every { tenantStore.getCurrentTenant() } returns Either.Right(VISIT_OWNER)
        every {
            visitStore.findPatientVisits(
                PATIENT_ID,
                VISIT_OWNER,
                pageable
            )
        } returns Either.Left(exception)

        //when
        val result = query.execute(GetPatientVisitsQuery.Input(PATIENT_ID, pageable))

        //then
        result shouldBeLeft exception
    }

    should("return error when tenant id is not found") {
        //given
        val pageable = Pageable.unpaged()
        val exception = Exception()
        every { tenantStore.getCurrentTenant() } returns Either.Left(exception)

        //when
        val result = query.execute(GetPatientVisitsQuery.Input(PATIENT_ID, pageable))

        //then
        result shouldBeLeft exception
    }
})