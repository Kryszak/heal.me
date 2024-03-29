package io.github.kryszak.healme.visit.adapter

import io.github.kryszak.healme.doctor.DOCTOR_ID
import io.github.kryszak.healme.doctor.DOCTOR_OWNER
import io.github.kryszak.healme.doctor.adapter.DoctorEntity
import io.github.kryszak.healme.doctor.testDoctor
import io.github.kryszak.healme.patient.PATIENT_ID
import io.github.kryszak.healme.patient.PATIENT_OWNER
import io.github.kryszak.healme.patient.adapter.PatientEntity
import io.github.kryszak.healme.patient.testPatient
import io.github.kryszak.healme.visit.*
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

class SqlVisitStoreTest : ShouldSpec({
    val visitRepository = mockk<VisitRepository>()
    val visitStore = SqlVisitStore(visitRepository)

    should("save new visit") {
        //given
        val params = CreateVisitParams(VISIT_DATE_TIME, VISIT_PLACE, testDoctor(), testPatient(), VISIT_OWNER)
        every { visitRepository.save(VisitEntity.fromParams(params)) } returns testVisitEntity()

        //when
        val result = visitStore.saveVisit(params)

        //then
        result.shouldBeRight()
    }

    should("return true if visit already exists for given time window") {
        //given
        val startTime = LocalDateTime.now()
        val endTime = startTime.plusMinutes(30L)
        val patient = testPatient()
        val doctor = testDoctor()
        val params = ExistsVisitInTimeWindowParams(
            startTime,
            endTime,
            patient,
            doctor
        )
        every {
            visitRepository.existsInGivenTimeFrame(
                startTime,
                endTime,
                PatientEntity.fromDomain(patient),
                DoctorEntity.fromDomain(doctor)
            )
        } returns true

        //when
        val result = visitStore.existsVisitInGivenTimeWindow(params)

        //then
        result shouldBeRight true
    }

    should("return false if visit doesn't exist for given time window") {
        //given
        val startTime = LocalDateTime.now()
        val endTime = startTime.plusMinutes(30L)
        val patient = testPatient()
        val doctor = testDoctor()
        val params = ExistsVisitInTimeWindowParams(
            startTime,
            endTime,
            patient,
            doctor
        )
        every {
            visitRepository.existsInGivenTimeFrame(
                startTime,
                endTime,
                PatientEntity.fromDomain(patient),
                DoctorEntity.fromDomain(doctor)
            )
        } returns false

        //when
        val result = visitStore.existsVisitInGivenTimeWindow(params)

        //then
        result shouldBeRight false
    }

    should("delete visits by patient id ") {
        //given
        every { visitRepository.deleteByPatientIdAndOwner(PATIENT_ID, PATIENT_OWNER.value) } returns 1L

        //when
        val result = visitStore.deleteByPatient(PATIENT_ID, PATIENT_OWNER)

        //then
        result.shouldBeRight()
    }

    should("delete visits by doctor id ") {
        //given
        every { visitRepository.deleteByDoctorIdAndOwner(DOCTOR_ID, DOCTOR_OWNER.value) } returns 1L

        //when
        val result = visitStore.deleteByDoctor(DOCTOR_ID, DOCTOR_OWNER)

        //then
        result.shouldBeRight()
    }

    should("retrieve visits list") {
        //given
        val pageable = Pageable.unpaged()
        every {
            visitRepository.findAllByOwner(
                VISIT_OWNER.value,
                pageable
            )
        } returns PageImpl(listOf(testVisitEntity()))

        //when
        val result = visitStore.findVisits(VISIT_OWNER, pageable)

        //then
        result shouldBeRight PageImpl(listOf(testVisit()))
    }

    should("find visit by id") {
        //given
        every { visitRepository.findByIdAndOwner(VISIT_ID, VISIT_OWNER.value) } returns testVisitEntity()

        //when
        val result = visitStore.findVisit(VISIT_ID, VISIT_OWNER)

        //then
        result shouldBeRight testVisit()
    }

    should("retrieve patient's visits list") {
        //given
        val pageable = Pageable.unpaged()
        every {
            visitRepository.findAllByPatientIdAndOwner(
                PATIENT_ID,
                VISIT_OWNER.value,
                pageable
            )
        } returns PageImpl(listOf(testVisitEntity()))

        //when
        val result = visitStore.findPatientVisits(PATIENT_ID, VISIT_OWNER, pageable)

        //then
        result shouldBeRight PageImpl(listOf(testVisit()))
    }

    should("update visit") {
        //given
        val visit = testVisit()
        every { visitRepository.save(testVisitEntity()) } returns testVisitEntity()

        //when
        val result = visitStore.updateVisit(visit)

        //then
        result shouldBeRight visit
    }
})