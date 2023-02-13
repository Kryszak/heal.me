package net.kryszak.healme.patient.adapter

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import java.util.UUID
import net.kryszak.healme.authentication.TenantId
import net.kryszak.healme.patient.CreatePatientParams
import net.kryszak.healme.patient.Patient

class SqlPatientStoreTest : ShouldSpec({

    val patientRepository = mockk<PatientRepository>()
    val patientStore = SqlPatientStore(patientRepository)

    should("create new patient") {
        // given
        val createdId = 1L
        val name = "Jan"
        val surname = "Random"
        val address = "Losowa street 2, Randomize Town"
        val owner = TenantId(UUID.randomUUID())
        val params = CreatePatientParams(name, surname, address, owner)
        val savedEntity = PatientEntity().apply {
            this.id = createdId
            this.name = name
            this.surname = surname
            this.address = address
            this.owner = owner.value
        }
        every { patientRepository.save(PatientEntity.from(params)) } returns savedEntity

        //when
        val result = patientStore.savePatient(params)

        //then
        result shouldBeRight Patient(createdId, name, surname, address, owner)
    }

    should("return exception if saving new patient failed") {
        //given
        val params =
            CreatePatientParams("Jan", "Random", "Losowa street 2, Randomize Town", TenantId(UUID.randomUUID()))
        val exception = Exception()
        every { patientRepository.save(PatientEntity.from(params)) } throws exception

        //when
        val result = patientStore.savePatient(params)

        //then
        result shouldBeLeft exception
    }
})