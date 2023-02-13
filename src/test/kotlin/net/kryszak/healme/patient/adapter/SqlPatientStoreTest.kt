package net.kryszak.healme.patient.adapter

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import java.util.UUID
import net.kryszak.healme.authentication.TenantId
import net.kryszak.healme.patient.*
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class SqlPatientStoreTest : ShouldSpec({

    val patientRepository = mockk<PatientRepository>()
    val patientStore = SqlPatientStore(patientRepository)

    should("create new patient") {
        // given
        val params = CreatePatientParams(PATIENT_NAME, PATIENT_SURNAME, PATIENT_ADDRESS, PATIENT_OWNER)
        every { patientRepository.save(PatientEntity.from(params)) } returns testPatientEntity()

        //when
        val result = patientStore.savePatient(params)

        //then
        result shouldBeRight testPatient()
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

    should("retrieve patient list") {
        //given
        val pageable = Pageable.unpaged()
        every {
            patientRepository.findAllByOwner(
                PATIENT_OWNER.value,
                pageable
            )
        } returns PageImpl(listOf(testPatientEntity()))

        //when
        val result = patientStore.findPatients(PATIENT_OWNER, pageable)

        //then
        result shouldBeRight PageImpl(listOf(testPatient()))
    }
})