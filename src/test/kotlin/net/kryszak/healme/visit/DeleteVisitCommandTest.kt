package net.kryszak.healme.visit

import arrow.core.Either
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import net.kryszak.healme.common.TenantStore
import net.kryszak.healme.common.exception.DataNotFoundException

class DeleteVisitCommandTest : ShouldSpec({
    val visitStore = mockk<VisitStore>()
    val tenantStore = mockk<TenantStore>()
    val command = DeleteVisitCommand(visitStore, tenantStore)

    should("delete visit") {
        //given
        val visit = testVisit()
        every { tenantStore.getCurrentTenant() } returns Either.Right(VISIT_OWNER)
        every { visitStore.findVisit(VISIT_ID, VISIT_OWNER) } returns Either.Right(visit)
        every { visitStore.deleteVisit(visit) } returns Either.Right(Unit)

        //when
        val result = command.execute(DeleteVisitCommand.Input(VISIT_ID))

        //then
        result.shouldBeRight()
    }

    should("not delete visit if visit is not found") {
        //given
        val exception = DataNotFoundException()
        every { tenantStore.getCurrentTenant() } returns Either.Right(VISIT_OWNER)
        every { visitStore.findVisit(VISIT_ID, VISIT_OWNER) } returns Either.Left(exception)

        //when
        val result = command.execute(DeleteVisitCommand.Input(VISIT_ID))

        //then
        result shouldBeLeft exception
    }

    should("not delete visit if tenant is not found") {
        //given
        val exception = Exception()
        every { tenantStore.getCurrentTenant() } returns Either.Left(exception)

        //when
        val result = command.execute(
            DeleteVisitCommand.Input(VISIT_ID)
        )

        //then
        result shouldBeLeft exception
    }
})