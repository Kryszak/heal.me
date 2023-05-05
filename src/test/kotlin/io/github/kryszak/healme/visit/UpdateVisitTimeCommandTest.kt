package io.github.kryszak.healme.visit

import arrow.core.Either
import io.github.kryszak.healme.common.TenantStore
import io.github.kryszak.healme.common.exception.DataNotFoundException
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import java.time.Duration
import java.time.LocalTime

class UpdateVisitTimeCommandTest : ShouldSpec({
    val visitStore = mockk<VisitStore>()
    val tenantStore = mockk<TenantStore>()
    val visitDuration = Duration.ofMinutes(30)
    val command = UpdateVisitTimeCommand(visitStore, tenantStore, visitDuration)

    should("update visit time") {
        //given
        val visit = testVisit()
        val dateTime = visit.dateTime
        val adjustedTime = LocalTime.now()
        val updatedVisit = visit.copy(dateTime = visit.dateTime.with(adjustedTime))
        val input = UpdateVisitTimeCommand.Input(visit.id, adjustedTime)
        every { tenantStore.getCurrentTenant() } returns Either.Right(VISIT_OWNER)
        every { visitStore.findVisit(VISIT_ID, VISIT_OWNER) } returns Either.Right(visit)
        every {
            visitStore.existsVisitInGivenTimeWindow(
                ExistsVisitInTimeWindowParams(
                    dateTime.with(adjustedTime).minus(visitDuration),
                    dateTime.with(adjustedTime).plus(visitDuration),
                    visit.patient,
                    visit.doctor
                )
            )
        } returns Either.Right(false)
        every { visitStore.updateVisit(updatedVisit) } returns Either.Right(updatedVisit)

        //when
        val result = command.execute(input)

        //then
        result.shouldBeRight()
    }

    should("return exception if another visit is planned in given time") {
        //given
        val visit = testVisit()
        val dateTime = visit.dateTime
        val adjustedTime = LocalTime.now()
        val input = UpdateVisitTimeCommand.Input(visit.id, adjustedTime)
        every { tenantStore.getCurrentTenant() } returns Either.Right(VISIT_OWNER)
        every { visitStore.findVisit(VISIT_ID, VISIT_OWNER) } returns Either.Right(visit)
        every {
            visitStore.existsVisitInGivenTimeWindow(
                ExistsVisitInTimeWindowParams(
                    dateTime.with(adjustedTime).minus(visitDuration),
                    dateTime.with(adjustedTime).plus(visitDuration),
                    visit.patient,
                    visit.doctor
                )
            )
        } returns Either.Right(true)

        //when
        val result = command.execute(input)

        //then
        result shouldBeLeft VisitTimeAlreadyTakenException("Visit for given time frame with doctor or patient already exists.")
    }

    should("return exception if tenant is not found") {
        //given
        val input = UpdateVisitTimeCommand.Input(VISIT_ID, LocalTime.now())
        val exception = DataNotFoundException()
        every { tenantStore.getCurrentTenant() } returns Either.Left(exception)

        //when
        val result = command.execute(input)

        //then
        result shouldBeLeft exception
    }

    should("return exception if saving visit fails") {
        //given
        val visit = testVisit()
        val newTime = LocalTime.now()
        val input = UpdateVisitTimeCommand.Input(VISIT_ID, newTime)
        val exception = Exception()

        every { tenantStore.getCurrentTenant() } returns Either.Right(VISIT_OWNER)
        every { visitStore.findVisit(VISIT_ID, VISIT_OWNER) } returns Either.Right(visit)
        every {
            visitStore.existsVisitInGivenTimeWindow(
                ExistsVisitInTimeWindowParams(
                    visit.dateTime.with(newTime).minus(visitDuration),
                    visit.dateTime.with(newTime).plus(visitDuration),
                    visit.patient,
                    visit.doctor
                )
            )
        } returns Either.Right(false)
        every { visitStore.updateVisit(visit.copy(dateTime = visit.dateTime.with(newTime))) } returns Either.Left(
            exception
        )

        //when
        val result = command.execute(input)

        //then
        result shouldBeLeft exception
    }
})