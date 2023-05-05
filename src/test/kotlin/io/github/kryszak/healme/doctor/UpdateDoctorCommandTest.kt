package io.github.kryszak.healme.doctor

import arrow.core.Either
import io.github.kryszak.healme.common.TenantStore
import io.github.kryszak.healme.common.exception.DataMismatchException
import io.github.kryszak.healme.common.exception.DataNotFoundException
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk

class UpdateDoctorCommandTest : ShouldSpec({
    val updatedName = "Updated name"
    val updatedSurname = "Updated surname"
    val updatedSpecialization = "Updated specialization"

    val doctorStore = mockk<DoctorStore>()
    val tenantStore = mockk<TenantStore>()
    val command = UpdateDoctorCommand(doctorStore, tenantStore)

    should("update doctor") {
        //given
        val updatedDoctor = testDoctor().copy(
                name = updatedName,
                surname = updatedSurname,
                specialization = updatedSpecialization
        )
        every { tenantStore.getCurrentTenant() } returns Either.Right(DOCTOR_OWNER)
        every { doctorStore.findDoctor(DOCTOR_OWNER, DOCTOR_ID) } returns Either.Right(testDoctor())
        every { doctorStore.updateDoctor(updatedDoctor) } returns Either.Right(updatedDoctor)

        //when
        val result = command.execute(UpdateDoctorCommand.Input(DOCTOR_ID, DOCTOR_ID, updatedName, updatedSurname, updatedSpecialization))

        //then
        result shouldBeRight updatedDoctor
    }

    should("not update doctor if resource id does not match doctor id") {
        //when
        val result = command.execute(UpdateDoctorCommand.Input(2L, DOCTOR_ID, updatedName, updatedSurname, updatedSpecialization))

        //then
        result shouldBeLeft DataMismatchException()
    }

    should("not update doctor if doctor is not found") {
        //given
        val exception = DataNotFoundException()
        every { tenantStore.getCurrentTenant() } returns Either.Right(DOCTOR_OWNER)
        every { doctorStore.findDoctor(DOCTOR_OWNER, DOCTOR_ID) } returns Either.Left(exception)

        //when
        val result = command.execute(UpdateDoctorCommand.Input(DOCTOR_ID, DOCTOR_ID, updatedName, updatedSurname, updatedSpecialization))

        //then
        result shouldBeLeft exception
    }

    should("not update doctor if tenant is not found") {
        //given
        val exception = Exception()
        every { tenantStore.getCurrentTenant() } returns Either.Left(exception)

        //when
        val result = command.execute(
            UpdateDoctorCommand.Input(
                DOCTOR_ID,
                DOCTOR_ID,
                DOCTOR_NAME,
                DOCTOR_SURNAME,
                DOCTOR_SPECIALIZATION
            )
        )

        //then
        result shouldBeLeft exception
    }
})