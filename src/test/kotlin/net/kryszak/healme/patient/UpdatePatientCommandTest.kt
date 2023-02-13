package net.kryszak.healme.patient

import arrow.core.Either
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import net.kryszak.healme.common.TenantStore
import net.kryszak.healme.common.exception.DataMismatchException
import net.kryszak.healme.common.exception.DataNotFoundException
import net.kryszak.healme.patient.UpdatePatientCommand.Input

class UpdatePatientCommandTest : ShouldSpec({
    val patientStore = mockk<PatientStore>()
    val tenantStore = mockk<TenantStore>()
    val command = UpdatePatientCommand(patientStore, tenantStore)

    should("update patient") {
        //given
        val name = "Updated name"
        val surname = "Updated surname"
        val address = "Updated address"
        val updatedPatient = testPatient().copy(
            name = name,
            surname = surname,
            address = address
        )
        every { tenantStore.getCurrentTenant() } returns Either.Right(PATIENT_OWNER)
        every { patientStore.findPatient(PATIENT_OWNER, PATIENT_ID) } returns Either.Right(testPatient())
        every { patientStore.updatePatient(updatedPatient) } returns Either.Right(updatedPatient)

        //when
        val result = command.execute(Input(PATIENT_ID, PATIENT_ID, name, surname, address))

        //then
        result shouldBeRight updatedPatient
    }

    should("not update patient if resource id does not match patient id") {
        //given
        val name = "Updated name"
        val surname = "Updated surname"
        val address = "Updated address"

        //when
        val result = command.execute(Input(2L, PATIENT_ID, name, surname, address))

        //then
        result shouldBeLeft DataMismatchException()
    }

    should("not update patient if patient is not found") {
        //given
        val name = "Updated name"
        val surname = "Updated surname"
        val address = "Updated address"
        val exception = DataNotFoundException()
        every { tenantStore.getCurrentTenant() } returns Either.Right(PATIENT_OWNER)
        every { patientStore.findPatient(PATIENT_OWNER, PATIENT_ID) } returns Either.Left(exception)

        //when
        val result = command.execute(Input(PATIENT_ID, PATIENT_ID, name, surname, address))

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