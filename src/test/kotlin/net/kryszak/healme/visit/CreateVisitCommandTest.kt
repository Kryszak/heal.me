package net.kryszak.healme.visit

import arrow.core.Either
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import java.time.Duration
import java.time.LocalDateTime
import net.kryszak.healme.common.TenantStore
import net.kryszak.healme.common.exception.DataNotFoundException
import net.kryszak.healme.doctor.DOCTOR_ID
import net.kryszak.healme.doctor.testDoctor
import net.kryszak.healme.patient.PATIENT_ID
import net.kryszak.healme.patient.testPatient

class CreateVisitCommandTest : ShouldSpec({
    val doctorStore = mockk<DoctorStore>()
    val patientStore = mockk<PatientStore>()
    val visitStore = mockk<VisitStore>()
    val tenantStore = mockk<TenantStore>()
    val visitDuration = Duration.ofMinutes(30)
    val command = CreateVisitCommand(doctorStore, patientStore, visitStore, tenantStore, visitDuration)

    should("create new visit") {
        //given
        val doctorId = DOCTOR_ID
        val patientId = PATIENT_ID
        val doctor = testDoctor()
        val patient = testPatient()
        val dateTime = LocalDateTime.now()
        val place = "place"
        val input = CreateVisitCommand.Input(doctorId, patientId, dateTime, place)
        every { tenantStore.getCurrentTenant() } returns Either.Right(VISIT_OWNER)
        every { doctorStore.getDoctor(doctorId) } returns Either.Right(doctor)
        every { patientStore.getPatient(patientId) } returns Either.Right(patient)
        every {
            visitStore.existsVisitInGivenTimeWindow(
                ExistsVisitInTimeWindowParams(
                    dateTime.minus(visitDuration),
                    dateTime.plus(visitDuration),
                    patient,
                    doctor
                )
            )
        } returns Either.Right(false)
        every {
            visitStore.saveVisit(
                CreateVisitParams(
                    dateTime,
                    place,
                    doctor,
                    patient,
                    VISIT_OWNER
                )
            )
        } returns Either.Right(
            testVisit()
        )

        //when
        val result = command.execute(input)

        //then
        result.shouldBeRight()
    }

    should("return exception if another visit is planned in given time") {
        //given
        val doctorId = DOCTOR_ID
        val patientId = PATIENT_ID
        val doctor = testDoctor()
        val patient = testPatient()
        val dateTime = LocalDateTime.now()
        val place = "place"
        val input = CreateVisitCommand.Input(doctorId, patientId, dateTime, place)
        every { tenantStore.getCurrentTenant() } returns Either.Right(VISIT_OWNER)
        every { doctorStore.getDoctor(doctorId) } returns Either.Right(doctor)
        every { patientStore.getPatient(patientId) } returns Either.Right(patient)
        every {
            visitStore.existsVisitInGivenTimeWindow(
                ExistsVisitInTimeWindowParams(
                    dateTime.minus(visitDuration),
                    dateTime.plus(visitDuration),
                    patient,
                    doctor
                )
            )
        } returns Either.Right(true)

        //when
        val result = command.execute(input)

        //then
        result shouldBeLeft VisitTimeAlreadyTakenException("Visit for given time frame with doctor or patient already exists.")
    }

    should("return exception if tenant is not found") {
        //given
        val doctorId = DOCTOR_ID
        val patientId = PATIENT_ID
        val dateTime = LocalDateTime.now()
        val place = "place"
        val input = CreateVisitCommand.Input(doctorId, patientId, dateTime, place)
        val exception = DataNotFoundException()
        every { tenantStore.getCurrentTenant() } returns Either.Left(exception)
        every { doctorStore.getDoctor(doctorId) } returns Either.Right(testDoctor())
        every { patientStore.getPatient(patientId) } returns Either.Right(testPatient())

        //when
        val result = command.execute(input)

        //then
        result shouldBeLeft exception
    }

    should("return exception if patient is not found") {
        //given
        val doctorId = DOCTOR_ID
        val patientId = PATIENT_ID
        val dateTime = LocalDateTime.now()
        val place = "place"
        val input = CreateVisitCommand.Input(doctorId, patientId, dateTime, place)
        val exception = DataNotFoundException()
        every { tenantStore.getCurrentTenant() } returns Either.Right(VISIT_OWNER)
        every { doctorStore.getDoctor(doctorId) } returns Either.Right(testDoctor())
        every { patientStore.getPatient(patientId) } returns Either.Left(exception)

        //when
        val result = command.execute(input)

        //then
        result shouldBeLeft exception
    }

    should("return exception if doctor is not found") {
        //given
        val doctorId = DOCTOR_ID
        val patientId = PATIENT_ID
        val dateTime = LocalDateTime.now()
        val place = "place"
        val input = CreateVisitCommand.Input(doctorId, patientId, dateTime, place)
        val exception = DataNotFoundException()
        every { tenantStore.getCurrentTenant() } returns Either.Right(VISIT_OWNER)
        every { doctorStore.getDoctor(doctorId) } returns Either.Left(exception)
        every { patientStore.getPatient(patientId) } returns Either.Right(testPatient())

        //when
        val result = command.execute(input)

        //then
        result shouldBeLeft exception
    }

    should("return exception if saving visit fails") {
        //given
        val doctorId = DOCTOR_ID
        val patientId = PATIENT_ID
        val doctor = testDoctor()
        val patient = testPatient()
        val dateTime = LocalDateTime.now()
        val place = "place"
        val input = CreateVisitCommand.Input(doctorId, patientId, dateTime, place)
        val exception = Exception()

        every { tenantStore.getCurrentTenant() } returns Either.Right(VISIT_OWNER)
        every { doctorStore.getDoctor(doctorId) } returns Either.Right(doctor)
        every { patientStore.getPatient(patientId) } returns Either.Right(patient)
        every {
            visitStore.existsVisitInGivenTimeWindow(
                ExistsVisitInTimeWindowParams(
                    dateTime.minus(visitDuration),
                    dateTime.plus(visitDuration),
                    patient,
                    doctor
                )
            )
        } returns Either.Right(false)
        every {
            visitStore.saveVisit(
                CreateVisitParams(
                    dateTime,
                    place,
                    doctor,
                    patient,
                    VISIT_OWNER
                )
            )
        } returns Either.Left(exception)

        //when
        val result = command.execute(input)

        //then
        result shouldBeLeft exception
    }
})