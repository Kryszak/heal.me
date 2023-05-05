package io.github.kryszak.healme.patient

import arrow.core.Either
import io.github.kryszak.healme.common.TenantStore
import io.github.kryszak.healme.common.exception.DataNotFoundException
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk

class GetPatientQueryTest : ShouldSpec({
    val patientStore = mockk<PatientStore>()
    val tenantStore = mockk<TenantStore>()
    val query = GetPatientQuery(patientStore, tenantStore)

    should("retrieve patient") {
        //given
        every { tenantStore.getCurrentTenant() } returns Either.Right(PATIENT_OWNER)
        every {
            patientStore.findPatient(
                PATIENT_OWNER,
                PATIENT_ID
            )
        } returns Either.Right(testPatient())

        //when
        val result = query.execute(GetPatientQuery.Input(PATIENT_ID))

        //then
        result shouldBeRight testPatient()
    }

    should("return error when fetching patient does not exist") {
        //given
        every { tenantStore.getCurrentTenant() } returns Either.Right(PATIENT_OWNER)
        every {
            patientStore.findPatient(
                PATIENT_OWNER,
                PATIENT_ID
            )
        } returns Either.Left(DataNotFoundException())

        //when
        val result = query.execute(GetPatientQuery.Input(PATIENT_ID))

        //then
        result shouldBeLeft DataNotFoundException()
    }

    should("return error when tenant id is not found") {
        //given
        val exception = Exception()
        every { tenantStore.getCurrentTenant() } returns Either.Left(exception)

        //when
        val result = query.execute(GetPatientQuery.Input(PATIENT_ID))

        //then
        result shouldBeLeft exception
    }
})