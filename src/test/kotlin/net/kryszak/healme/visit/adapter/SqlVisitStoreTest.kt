package net.kryszak.healme.visit.adapter

import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import net.kryszak.healme.doctor.adapter.DoctorEntity
import net.kryszak.healme.doctor.testDoctor
import net.kryszak.healme.patient.adapter.PatientEntity
import net.kryszak.healme.patient.testPatient
import net.kryszak.healme.visit.*

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
})