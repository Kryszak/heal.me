package net.kryszak.healme.doctor.adapter

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import net.kryszak.healme.doctor.*

class SqlDoctorStoreTest : ShouldSpec({
    val doctorRepository = mockk<DoctorRepository>()
    val doctorStore = SqlDoctorStore(doctorRepository)

    should("create new doctor") {
        // given
        val params = CreateDoctorParams(DOCTOR_NAME, DOCTOR_SURNAME, DOCTOR_SPECIALIZATION, DOCTOR_OWNER)
        every { doctorRepository.save(DoctorEntity.fromParams(params)) } returns testDoctorEntity()

        //when
        val result = doctorStore.saveDoctor(params)

        //then
        result shouldBeRight testDoctor()
    }

    should("return exception if saving new doctor failed") {
        //given
        val params =
            CreateDoctorParams(DOCTOR_NAME, DOCTOR_SURNAME, DOCTOR_SPECIALIZATION, DOCTOR_OWNER)
        val exception = Exception()
        every { doctorRepository.save(DoctorEntity.fromParams(params)) } throws exception

        //when
        val result = doctorStore.saveDoctor(params)

        //then
        result shouldBeLeft exception
    }
})