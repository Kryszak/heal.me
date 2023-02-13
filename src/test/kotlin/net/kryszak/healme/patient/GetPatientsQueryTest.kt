package net.kryszak.healme.patient

import arrow.core.Either
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import net.kryszak.healme.common.TenantStore
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class GetPatientsQueryTest : ShouldSpec({

    val patientStore = mockk<PatientStore>()
    val tenantStore = mockk<TenantStore>()
    val query = GetPatientsQuery(patientStore, tenantStore)

    should("retrieve patients list") {
        //given
        val pageable = Pageable.unpaged()
        every { tenantStore.getCurrentTenant() } returns Either.Right(PATIENT_OWNER)
        val pagedPatients = PageImpl(listOf(testPatient()))
        every {
            patientStore.findPatients(
                PATIENT_OWNER,
                pageable
            )
        } returns Either.Right(pagedPatients)

        //when
        val result = query.execute(GetPatientsQuery.Input(pageable))

        //then
        result shouldBeRight pagedPatients
    }

    should("return error when fetching patients fails") {
        //given
        val pageable = Pageable.unpaged()
        val exception = Exception()
        every { tenantStore.getCurrentTenant() } returns Either.Right(PATIENT_OWNER)
        every {
            patientStore.findPatients(
                PATIENT_OWNER,
                pageable
            )
        } returns Either.Left(exception)

        //when
        val result = query.execute(GetPatientsQuery.Input(pageable))

        //then
        result shouldBeLeft exception
    }

    should("return error when tenant id is not found") {
        //given
        val pageable = Pageable.unpaged()
        val exception = Exception()
        every { tenantStore.getCurrentTenant() } returns Either.Left(exception)

        //when
        val result = query.execute(GetPatientsQuery.Input(pageable))

        //then
        result shouldBeLeft exception
    }
})