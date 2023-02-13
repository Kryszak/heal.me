package net.kryszak.healme.patient

import arrow.core.Either
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import java.util.UUID
import net.kryszak.healme.authentication.TenantId
import net.kryszak.healme.common.TenantStore
import net.kryszak.healme.patient.CreatePatientCommand.Input

class CreatePatientCommandTest : ShouldSpec({

    val patientStore = mockk<PatientStore>()
    val tenantStore = mockk<TenantStore>()
    val command = CreatePatientCommand(patientStore, tenantStore)

    should("create new patient") {
        //given
        val name = "Jan"
        val surname = "Random"
        val address = "Losowa street 2, Randomize Town"
        val tenantId = TenantId(UUID.randomUUID())
        val input = Input(name, surname, address)
        every { tenantStore.getCurrentTenant() } returns Either.Right(tenantId)
        every { patientStore.savePatient(CreatePatientParams(name, surname, address, tenantId)) } returns Either.Right(
            Patient(
                1,
                name,
                surname,
                address,
                tenantId
            )
        )

        //when
        val result = command.execute(input)

        //then
        result.shouldBeRight()
    }

    should("return exception if creation of new patient fails") {
        //given
        val name = "Jan"
        val surname = "Random"
        val address = "Losowa street 2, Randomize Town"
        val input = Input(name, surname, address)
        val exception = Exception()
        val tenantId = TenantId((UUID.randomUUID()))
        every { tenantStore.getCurrentTenant() } returns Either.Right(tenantId)
        every {
            patientStore.savePatient(
                CreatePatientParams(
                    name,
                    surname,
                    address,
                    tenantId
                )
            )
        } returns Either.Left(exception)

        //when
        val result = command.execute(input)

        //then
        result shouldBeLeft exception
    }
})