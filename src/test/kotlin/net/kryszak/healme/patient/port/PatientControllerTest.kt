package net.kryszak.healme.patient.port

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import net.kryszak.healme.authentication.*
import net.kryszak.healme.patient.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.*
import java.util.*


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Sql(scripts = ["classpath:test_db/tenant.sql"])
class PatientControllerTest : ShouldSpec() {

    override fun extensions() = listOf(SpringExtension)

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var patientStore: PatientStore

    init {
        val patientsUrl = "/patients"

        val updatedPatientName = "New name"
        val updatedPatientSurname = "New Surname"
        val updatedPatientAddress = "another address"

        should("create new patient") {
            //given
            val request = CreatePatientDto("Jan", "Random", "random address")

            //when & then
            mockMvc.post(patientsUrl) {
                header(API_KEY_HEADER, VALID_API_KEY)
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andExpect {
                status { isCreated() }
            }
        }

        should("return 403 response if tenant is not found") {
            //when & then
            mockMvc.get(patientsUrl) {
                header(API_KEY_HEADER, INVALID_API_KEY)
            }.andExpect {
                status { isForbidden() }
            }
        }

        should("retrieve patient list") {
            //given
            patientStore.savePatient(
                CreatePatientParams(
                    PATIENT_NAME,
                    PATIENT_SURNAME,
                    PATIENT_ADDRESS,
                    TENANT_ID
                )
            )

            //when & then
            mockMvc.get(patientsUrl) {
                header(API_KEY_HEADER, VALID_API_KEY)
            }.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath("\$.content[0].name") { isNotEmpty() } }
                content { jsonPath("\$.content[0].surname") { isNotEmpty() } }
                content { jsonPath("\$.content[0].address") { isNotEmpty() } }
            }
        }

        should("retrieve patient for given id") {
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
                .getOrNull()

            //when & then
            mockMvc.get("$patientsUrl/$patientId") {
                header(API_KEY_HEADER, VALID_API_KEY)
            }.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath("\$.name") { value(PATIENT_NAME) } }
                content { jsonPath("\$.surname") { value(PATIENT_SURNAME) } }
                content { jsonPath("\$.address") { value(PATIENT_ADDRESS) } }
            }
        }

        should("return 404 status if patient was not found") {
            //when & then
            mockMvc.get("$patientsUrl/10000") {
                header(API_KEY_HEADER, VALID_API_KEY)
            }.andExpect {
                status { isNotFound() }
            }
        }

        should("update patient") {
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
            val request = UpdatePatientDto(patientId, updatedPatientName, updatedPatientSurname, updatedPatientAddress)

            //when & then
            mockMvc.put("$patientsUrl/$patientId") {
                header(API_KEY_HEADER, VALID_API_KEY)
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath("\$.name") { value(updatedPatientName) } }
                content { jsonPath("\$.surname") { value(updatedPatientSurname) } }
                content { jsonPath("\$.address") { value(updatedPatientAddress) } }
            }
        }

        should("return 404 if attempted to update non existing patient") {
            //given
            val patientId = 10000L
            val request = UpdatePatientDto(patientId, updatedPatientName, updatedPatientSurname, updatedPatientAddress)

            //when & then
            mockMvc.put("$patientsUrl/$patientId") {
                header(API_KEY_HEADER, VALID_API_KEY)
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andExpect {
                status { isNotFound() }
            }
        }

        should("return 400 if attempted to update patient with id other than provided in url") {
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
                .getOrNull() ?: throw Exception()
            val request = UpdatePatientDto(2L, updatedPatientName, updatedPatientSurname, updatedPatientAddress)

            //when & then
            mockMvc.put("$patientsUrl/$patientId") {
                header(API_KEY_HEADER, VALID_API_KEY)
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andExpect {
                status { isBadRequest() }
            }
        }

        should("delete given patient") {
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
                .getOrNull()

            //when & then
            mockMvc.delete("$patientsUrl/$patientId") {
                header(API_KEY_HEADER, VALID_API_KEY)
            }.andExpect {
                status { isNoContent() }
            }
        }

        should("return 404 if attempted to delete not existing patient") {
            //when & then
            mockMvc.delete("$patientsUrl/10000") {
                header(API_KEY_HEADER, VALID_API_KEY)
            }.andExpect {
                status { isNotFound() }
            }
        }
    }
}
