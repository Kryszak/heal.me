package io.github.kryszak.healme.visit

import arrow.core.Either
import io.github.kryszak.healme.common.TenantStore
import io.github.kryszak.healme.doctor.DOCTOR_ID
import io.github.kryszak.healme.patient.PATIENT_ID
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk

class DeleteVisitsCommandTest : ShouldSpec({
    val visitStore = mockk<VisitStore>()
    val tenantStore = mockk<TenantStore>()
    val command = DeleteVisitsCommand(visitStore, tenantStore)

    should("delete visits by patient") {
        //given
        every { tenantStore.getCurrentTenant() } returns Either.Right(VISIT_OWNER)
        every { visitStore.deleteByPatient(PATIENT_ID, VISIT_OWNER) } returns Either.Right(Unit)

        //when
        val result = command.execute(DeleteVisitsCommand.Input(PATIENT_ID, DeleteVisitsCommand.DeleteBy.PATIENT))

        //then
        result.shouldBeRight()
    }

    should("return exception if deleting patient's visits fails") {
        //given
        val exception = Exception()
        every { tenantStore.getCurrentTenant() } returns Either.Right(VISIT_OWNER)
        every { visitStore.deleteByPatient(PATIENT_ID, VISIT_OWNER) } returns Either.Left(exception)

        //when
        val result = command.execute(DeleteVisitsCommand.Input(PATIENT_ID, DeleteVisitsCommand.DeleteBy.PATIENT))

        //then
        result shouldBeLeft exception
    }

    should("delete visits by doctor") {
        //given
        every { tenantStore.getCurrentTenant() } returns Either.Right(VISIT_OWNER)
        every { visitStore.deleteByDoctor(DOCTOR_ID, VISIT_OWNER) } returns Either.Right(Unit)

        //when
        val result = command.execute(DeleteVisitsCommand.Input(DOCTOR_ID, DeleteVisitsCommand.DeleteBy.DOCTOR))

        //then
        result.shouldBeRight()
    }

    should("return exception if deleting doctor's visits fails") {
        //given
        val exception = Exception()
        every { tenantStore.getCurrentTenant() } returns Either.Right(VISIT_OWNER)
        every { visitStore.deleteByDoctor(DOCTOR_ID, VISIT_OWNER) } returns Either.Left(exception)

        //when
        val result = command.execute(DeleteVisitsCommand.Input(DOCTOR_ID, DeleteVisitsCommand.DeleteBy.DOCTOR))

        //then
        result shouldBeLeft exception
    }
})