package net.kryszak.healme.doctor

import arrow.core.Either
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import net.kryszak.healme.common.TenantStore

class CreateDoctorCommandTest : ShouldSpec({
    val doctorStore = mockk<DoctorStore>()
    val tenantStore = mockk<TenantStore>()
    val command = CreateDoctorCommand(doctorStore, tenantStore)

    should("create new doctor") {
        //given
        val input = CreateDoctorCommand.Input(DOCTOR_NAME, DOCTOR_SURNAME, DOCTOR_SPECIALIZATION)
        every { tenantStore.getCurrentTenant() } returns Either.Right(DOCTOR_OWNER)
        every {
            doctorStore.saveDoctor(
                CreateDoctorParams(
                    DOCTOR_NAME,
                    DOCTOR_SURNAME,
                    DOCTOR_SPECIALIZATION,
                    DOCTOR_OWNER
                )
            )
        } returns Either.Right(testDoctor())

        //when
        val result = command.execute(input)

        //then
        result.shouldBeRight()
    }

    should("return exception if creation of new doctor fails") {
        //given
        val input = CreateDoctorCommand.Input(DOCTOR_NAME, DOCTOR_SURNAME, DOCTOR_SPECIALIZATION)
        val exception = Exception()
        every { tenantStore.getCurrentTenant() } returns Either.Right(DOCTOR_OWNER)
        every {
            doctorStore.saveDoctor(
                CreateDoctorParams(
                    DOCTOR_NAME,
                    DOCTOR_SURNAME,
                    DOCTOR_SPECIALIZATION,
                    DOCTOR_OWNER
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
        val result = command.execute(CreateDoctorCommand.Input(DOCTOR_NAME, DOCTOR_SURNAME, DOCTOR_SPECIALIZATION))

        //then
        result shouldBeLeft exception
    }
})