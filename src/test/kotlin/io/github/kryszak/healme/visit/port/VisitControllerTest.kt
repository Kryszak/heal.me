package io.github.kryszak.healme.visit.port

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.kryszak.healme.authentication.API_KEY_HEADER
import io.github.kryszak.healme.authentication.INVALID_API_KEY
import io.github.kryszak.healme.authentication.TENANT_ID
import io.github.kryszak.healme.authentication.VALID_API_KEY
import io.github.kryszak.healme.doctor.*
import io.github.kryszak.healme.patient.*
import io.github.kryszak.healme.visit.CreateVisitParams
import io.github.kryszak.healme.visit.VISIT_PLACE
import io.github.kryszak.healme.visit.Visit
import io.github.kryszak.healme.visit.VisitStore
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.*
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Sql(scripts = ["classpath:test_db/tenant.sql"])
@ApplyExtension(SpringExtension::class)
class VisitControllerTest : ShouldSpec() {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var visitStore: VisitStore

    @Autowired
    lateinit var patientStore: PatientStore

    @Autowired
    lateinit var doctorStore: DoctorStore

    init {
        val visitsUrl = "/visits"

        should("create new visit") {
            //given
            val patientId = patientStore.savePatient(
                CreatePatientParams(
                    PATIENT_NAME,
                    PATIENT_SURNAME,
                    PATIENT_ADDRESS,
                    TENANT_ID
                )
            )
                .map(Patient::id)
                .getOrNull()!!

            val doctorId = doctorStore.saveDoctor(
                CreateDoctorParams(
                    DOCTOR_NAME,
                    DOCTOR_SURNAME,
                    DOCTOR_SPECIALIZATION,
                    TENANT_ID
                )
            )
                .map(Doctor::id)
                .getOrNull()!!

            val request = CreateVisitDto("2023-02-01", "15:00", "Medical place", doctorId, patientId)

            //when & then
            mockMvc.post(visitsUrl) {
                header(API_KEY_HEADER, VALID_API_KEY)
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andExpect {
                status { isCreated() }
            }
        }

        should("return 403 response if tenant is not found") {
            //when & then
            mockMvc.get(visitsUrl) {
                header(API_KEY_HEADER, INVALID_API_KEY)
            }.andExpect {
                status { isForbidden() }
            }
        }

        should("delete visit") {
            //given
            val patient = patientStore.savePatient(
                CreatePatientParams(
                    PATIENT_NAME,
                    PATIENT_SURNAME,
                    PATIENT_ADDRESS,
                    TENANT_ID
                )
            ).getOrNull()!!

            val doctor = doctorStore.saveDoctor(
                CreateDoctorParams(
                    DOCTOR_NAME,
                    DOCTOR_SURNAME,
                    DOCTOR_SPECIALIZATION,
                    TENANT_ID
                )
            ).getOrNull()!!

            val visitId = visitStore.saveVisit(
                CreateVisitParams(
                    LocalDateTime.now(),
                    VISIT_PLACE,
                    doctor,
                    patient,
                    TENANT_ID
                )
            )
                .map(Visit::id)
                .getOrNull()

            //when & then
            mockMvc.delete("$visitsUrl/$visitId") {
                header(API_KEY_HEADER, VALID_API_KEY)
            }.andExpect {
                status { isNoContent() }
            }
        }

        should("return 404 if attempted to delete not existing visit") {
            //when & then
            mockMvc.delete("$visitsUrl/10000") {
                header(API_KEY_HEADER, VALID_API_KEY)
            }.andExpect {
                status { isNotFound() }
            }
        }

        should("retrieve visit list") {
            //given
            val patient = patientStore.savePatient(
                CreatePatientParams(
                    PATIENT_NAME,
                    PATIENT_SURNAME,
                    PATIENT_ADDRESS,
                    TENANT_ID
                )
            ).getOrNull()!!

            val doctor = doctorStore.saveDoctor(
                CreateDoctorParams(
                    DOCTOR_NAME,
                    DOCTOR_SURNAME,
                    DOCTOR_SPECIALIZATION,
                    TENANT_ID
                )
            ).getOrNull()!!

            visitStore.saveVisit(
                CreateVisitParams(
                    LocalDateTime.now(),
                    VISIT_PLACE,
                    doctor,
                    patient,
                    TENANT_ID
                )
            )
                .map(Visit::id)
                .getOrNull()

            //when & then
            mockMvc.get(visitsUrl) {
                header(API_KEY_HEADER, VALID_API_KEY)
            }.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath("\$.content[0].id") { isNotEmpty() } }
                content { jsonPath("\$.content[0].date") { isNotEmpty() } }
                content { jsonPath("\$.content[0].time") { isNotEmpty() } }
                content { jsonPath("\$.content[0].place") { isNotEmpty() } }
                content { jsonPath("\$.content[0].doctor") { isNotEmpty() } }
                content { jsonPath("\$.content[0].patient") { isNotEmpty() } }
            }
        }

        should("retrieve visit list for patient") {
            //given
            val patient = patientStore.savePatient(
                CreatePatientParams(
                    PATIENT_NAME,
                    PATIENT_SURNAME,
                    PATIENT_ADDRESS,
                    TENANT_ID
                )
            ).getOrNull()!!

            val doctor = doctorStore.saveDoctor(
                CreateDoctorParams(
                    DOCTOR_NAME,
                    DOCTOR_SURNAME,
                    DOCTOR_SPECIALIZATION,
                    TENANT_ID
                )
            ).getOrNull()!!

            visitStore.saveVisit(
                CreateVisitParams(
                    LocalDateTime.now(),
                    VISIT_PLACE,
                    doctor,
                    patient,
                    TENANT_ID
                )
            )
                .map(Visit::id)
                .getOrNull()

            //when & then
            mockMvc.get("/patients/${patient.id}/visits") {
                header(API_KEY_HEADER, VALID_API_KEY)
            }.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath("\$.content[0].id") { isNotEmpty() } }
                content { jsonPath("\$.content[0].date") { isNotEmpty() } }
                content { jsonPath("\$.content[0].time") { isNotEmpty() } }
                content { jsonPath("\$.content[0].place") { isNotEmpty() } }
                content { jsonPath("\$.content[0].doctor") { isNotEmpty() } }
                content { jsonPath("\$.content[0].patient") { isNotEmpty() } }
            }
        }

        should("update time for given visit") {
            //given
            val request = UpdateVisitTimeDto(LocalTime.now().plusHours(1L).format(DateTimeFormatter.ofPattern("HH:mm")))
            val patient = patientStore.savePatient(
                CreatePatientParams(
                    PATIENT_NAME,
                    PATIENT_SURNAME,
                    PATIENT_ADDRESS,
                    TENANT_ID
                )
            ).getOrNull()!!

            val doctor = doctorStore.saveDoctor(
                CreateDoctorParams(
                    DOCTOR_NAME,
                    DOCTOR_SURNAME,
                    DOCTOR_SPECIALIZATION,
                    TENANT_ID
                )
            ).getOrNull()!!

            val visitId = visitStore.saveVisit(
                CreateVisitParams(
                    LocalDateTime.now(),
                    VISIT_PLACE,
                    doctor,
                    patient,
                    TENANT_ID
                )
            )
                .map(Visit::id)
                .getOrNull()

            //when & then
            mockMvc.patch("$visitsUrl/$visitId") {
                header(API_KEY_HEADER, VALID_API_KEY)
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andExpect {
                status { isNoContent() }
            }
        }
    }
}