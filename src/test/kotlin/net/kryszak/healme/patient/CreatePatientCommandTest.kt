package net.kryszak.healme.patient

import arrow.core.Either
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import net.kryszak.healme.common.TenantStore
import net.kryszak.healme.patient.CreatePatientCommand.Input

class CreatePatientCommandTest : ShouldSpec({

    val patientStore = mockk<PatientStore>()
    val tenantStore = mockk<TenantStore>()
    val command = CreatePatientCommand(patientStore, tenantStore)

    should("create new patient") {
        //given
        val input = Input(PATIENT_NAME, PATIENT_SURNAME, PATIENT_ADDRESS)
        every { tenantStore.getCurrentTenant() } returns Either.Right(PATIENT_OWNER)
        every {
            patientStore.savePatient(
                CreatePatientParams(
                    PATIENT_NAME,
                    PATIENT_SURNAME,
                    PATIENT_ADDRESS,
                    PATIENT_OWNER
                )
            )
        } returns Either.Right(testPatient())

        //when
        val result = command.execute(input)

        //then
        result.shouldBeRight()
    }

    should("return exception if creation of new patient fails") {
        //given
        val input = Input(PATIENT_NAME, PATIENT_SURNAME, PATIENT_ADDRESS)
        val exception = Exception()
        every { tenantStore.getCurrentTenant() } returns Either.Right(PATIENT_OWNER)
        every {
            patientStore.savePatient(
                CreatePatientParams(
                    PATIENT_NAME,
                    PATIENT_SURNAME,
                    PATIENT_ADDRESS,
                    PATIENT_OWNER
                )
            )
        } returns Either.Left(exception)

        //when
        val result = command.execute(input)

        //then
        result shouldBeLeft exception
    }

    should("return error when tenant id is not found") {
        //given
        val exception = Exception()
        every { tenantStore.getCurrentTenant() } returns Either.Left(exception)

        //when
        val result = command.execute(Input(PATIENT_NAME, PATIENT_SURNAME, PATIENT_ADDRESS))

        //then
        result shouldBeLeft exception
    }
})