package io.github.kryszak.healme.doctor

import arrow.core.Either
import io.github.kryszak.healme.common.TenantStore
import io.github.kryszak.healme.common.exception.DataNotFoundException
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk

class DeleteDoctorCommandTest : ShouldSpec({
    val doctorStore = mockk<DoctorStore>()
    val visitStore = mockk<VisitStore>()
    val tenantStore = mockk<TenantStore>()
    val command = DeleteDoctorCommand(doctorStore, visitStore, tenantStore)

    should("delete doctor") {
        //given
        val doctor = testDoctor()
        every { tenantStore.getCurrentTenant() } returns Either.Right(DOCTOR_OWNER)
        every { doctorStore.findDoctor(DOCTOR_OWNER, DOCTOR_ID) } returns Either.Right(doctor)
        every { visitStore.deleteVisits(doctor.id) } returns Either.Right(Unit)
        every { doctorStore.deleteDoctor(doctor) } returns Either.Right(Unit)

        //when
        val result = command.execute(DeleteDoctorCommand.Input(DOCTOR_ID))

        //then
        result.shouldBeRight()
    }

    should("not delete doctor if doctor is not found") {
        //given
        val exception = DataNotFoundException()
        every { tenantStore.getCurrentTenant() } returns Either.Right(DOCTOR_OWNER)
        every { doctorStore.findDoctor(DOCTOR_OWNER, DOCTOR_ID) } returns Either.Left(exception)

        //when
        val result = command.execute(DeleteDoctorCommand.Input(DOCTOR_ID))

        //then
        result shouldBeLeft exception
    }

    should("not delete doctor if tenant is not found") {
        //given
        val exception = Exception()
        every { tenantStore.getCurrentTenant() } returns Either.Left(exception)

        //when
        val result = command.execute(DeleteDoctorCommand.Input(DOCTOR_ID))

        //then
        result shouldBeLeft exception
    }

    should("not delete doctor if doctor's visits deletion fails") {
        //given
        val doctor = testDoctor()
        val exception = Exception()
        every { tenantStore.getCurrentTenant() } returns Either.Right(DOCTOR_OWNER)
        every { doctorStore.findDoctor(DOCTOR_OWNER, DOCTOR_ID) } returns Either.Right(doctor)
        every { visitStore.deleteVisits(doctor.id) } returns Either.Left(exception)

        //when
        val result = command.execute(DeleteDoctorCommand.Input(DOCTOR_ID))

        //then
        result shouldBeLeft exception
    }
})