package net.kryszak.healme.patient

import arrow.core.Either
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import net.kryszak.healme.common.TenantStore
import net.kryszak.healme.common.exception.DataNotFoundException

class DeletePatientCommandTest : ShouldSpec({
    val patientStore = mockk<PatientStore>()
    val visitStore = mockk<VisitStore>()
    val tenantStore = mockk<TenantStore>()
    val command = DeletePatientCommand(patientStore, visitStore, tenantStore)

    should("delete patient") {
        //given
        val patient = testPatient()
        every { tenantStore.getCurrentTenant() } returns Either.Right(PATIENT_OWNER)
        every { patientStore.findPatient(PATIENT_OWNER, PATIENT_ID) } returns Either.Right(patient)
        every { visitStore.deleteVisits(patient.id) } returns Either.Right(Unit)
        every { patientStore.deletePatient(patient) } returns Either.Right(Unit)

        //when
        val result = command.execute(DeletePatientCommand.Input(PATIENT_ID))

        //then
        result.shouldBeRight()
    }

    should("not delete patient if patient is not found") {
        //given
        val exception = DataNotFoundException()
        every { tenantStore.getCurrentTenant() } returns Either.Right(PATIENT_OWNER)
        every { patientStore.findPatient(PATIENT_OWNER, PATIENT_ID) } returns Either.Left(exception)

        //when
        val result = command.execute(DeletePatientCommand.Input(PATIENT_ID))

        //then
        result shouldBeLeft exception
    }

    should("not delete patient if tenant is not found") {
        //given
        val exception = Exception()
        every { tenantStore.getCurrentTenant() } returns Either.Left(exception)

        //when
        val result = command.execute(
            DeletePatientCommand.Input(PATIENT_ID)
        )

        //then
        result shouldBeLeft exception
    }

    should("not delete patient if patient's visits deletion fails") {
        //given
        val patient = testPatient()
        val exception = Exception()
        every { tenantStore.getCurrentTenant() } returns Either.Right(PATIENT_OWNER)
        every { patientStore.findPatient(PATIENT_OWNER, PATIENT_ID) } returns Either.Right(patient)
        every { visitStore.deleteVisits(patient.id) } returns Either.Left(exception)

        //when
        val result = command.execute(DeletePatientCommand.Input(PATIENT_ID))

        //then
        result shouldBeLeft exception
    }
})