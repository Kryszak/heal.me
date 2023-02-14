package net.kryszak.healme.doctor.adapter

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import net.kryszak.healme.common.exception.DataNotFoundException
import net.kryszak.healme.doctor.*
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

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

    should("retrieve doctor list") {
        //given
        val pageable = Pageable.unpaged()
        every {
            doctorRepository.findAllByOwner(
                DOCTOR_OWNER.value,
                pageable
            )
        } returns PageImpl(listOf(testDoctorEntity()))

        //when
        val result = doctorStore.findDoctors(DOCTOR_OWNER, pageable)

        //then
        result shouldBeRight PageImpl(listOf(testDoctor()))
    }

    should("find doctor") {
        //given
        every { doctorRepository.findByIdAndOwner(DOCTOR_ID, DOCTOR_OWNER.value) } returns testDoctorEntity()

        //when
        val result = doctorStore.findDoctor(DOCTOR_OWNER, DOCTOR_ID)

        //then
        result shouldBeRight testDoctor()
    }

    should("return error if doctor is not found") {
        //given
        every { doctorRepository.findByIdAndOwner(DOCTOR_ID, DOCTOR_OWNER.value) } returns null

        //when
        val result = doctorStore.findDoctor(DOCTOR_OWNER, DOCTOR_ID)

        //then
        result shouldBeLeft DataNotFoundException("Doctor with id={$DOCTOR_ID} not found under owner={$DOCTOR_OWNER}")
    }

    should("update doctor") {
        //given
        val doctor = testDoctor()
        every { doctorRepository.save(testDoctorEntity()) } returns testDoctorEntity()

        //when
        val result = doctorStore.updateDoctor(doctor)

        //then
        result shouldBeRight doctor
    }
})