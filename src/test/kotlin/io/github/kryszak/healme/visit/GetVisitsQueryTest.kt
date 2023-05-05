package io.github.kryszak.healme.visit

import arrow.core.Either
import io.github.kryszak.healme.common.TenantStore
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class GetVisitsQueryTest : ShouldSpec({
    val visitStore = mockk<VisitStore>()
    val tenantStore = mockk<TenantStore>()
    val query = GetVisitsQuery(visitStore, tenantStore)

    should("retrieve visits list") {
        //given
        val pageable = Pageable.unpaged()
        every { tenantStore.getCurrentTenant() } returns Either.Right(VISIT_OWNER)
        val pagedVisits = PageImpl(listOf(testVisit()))
        every {
            visitStore.findVisits(
                VISIT_OWNER,
                pageable
            )
        } returns Either.Right(pagedVisits)

        //when
        val result = query.execute(GetVisitsQuery.Input(pageable))

        //then
        result shouldBeRight pagedVisits
    }

    should("return error when fetching visits fails") {
        //given
        val pageable = Pageable.unpaged()
        val exception = Exception()
        every { tenantStore.getCurrentTenant() } returns Either.Right(VISIT_OWNER)
        every {
            visitStore.findVisits(
                VISIT_OWNER,
                pageable
            )
        } returns Either.Left(exception)

        //when
        val result = query.execute(GetVisitsQuery.Input(pageable))

        //then
        result shouldBeLeft exception
    }

    should("return error when tenant id is not found") {
        //given
        val pageable = Pageable.unpaged()
        val exception = Exception()
        every { tenantStore.getCurrentTenant() } returns Either.Left(exception)

        //when
        val result = query.execute(GetVisitsQuery.Input(pageable))

        //then
        result shouldBeLeft exception
    }
})