package io.github.kryszak.healme.patient

import arrow.core.Either
import io.github.kryszak.healme.common.TenantStore
import io.github.kryszak.healme.common.exception.DataMismatchException
import io.github.kryszak.healme.common.exception.DataNotFoundException
import io.github.kryszak.healme.patient.UpdatePatientCommand.Input
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk


class UpdatePatientCommandTest : ShouldSpec({
    val patientStore = mockk<PatientStore>()
    val tenantStore = mockk<TenantStore>()
    val command = UpdatePatientCommand(patientStore, tenantStore)

    val updatedName = "Updated name"
    val updatedSurname = "Updated surname"
    val updatedAddress = "Updated address"

    should("update patient") {
        //given
        val updatedPatient = testPatient().copy(
            name = updatedName,
            surname = updatedSurname,
            address = updatedAddress
        )
        every { tenantStore.getCurrentTenant() } returns Either.Right(PATIENT_OWNER)
        every { patientStore.findPatient(PATIENT_OWNER, PATIENT_ID) } returns Either.Right(testPatient())
        every { patientStore.updatePatient(updatedPatient) } returns Either.Right(updatedPatient)

        //when
        val result = command.execute(
            Input(
                PATIENT_ID,
                PATIENT_ID,
                updatedName,
                updatedSurname,
                updatedAddress
            )
        )

        //then
        result shouldBeRight updatedPatient
    }

    should("not update patient if resource id does not match patient id") {
        //when
        val result = command.execute(Input(2L, PATIENT_ID, updatedName, updatedSurname, updatedAddress))

        //then
        result shouldBeLeft DataMismatchException()
    }

    should("not update patient if patient is not found") {
        //given
        val exception = DataNotFoundException()
        every { tenantStore.getCurrentTenant() } returns Either.Right(PATIENT_OWNER)
        every { patientStore.findPatient(PATIENT_OWNER, PATIENT_ID) } returns Either.Left(exception)

        //when
        val result = command.execute(Input(PATIENT_ID, PATIENT_ID, updatedName, updatedSurname, updatedAddress))

        //then
        result shouldBeLeft exception
    }

    should("not update patient if tenant is not found") {
        //given
        val exception = Exception()
        every { tenantStore.getCurrentTenant() } returns Either.Left(exception)

        //when
        val result = command.execute(Input(PATIENT_ID, PATIENT_ID, PATIENT_NAME, PATIENT_SURNAME, PATIENT_ADDRESS))

        //then
        result shouldBeLeft exception
    }
})