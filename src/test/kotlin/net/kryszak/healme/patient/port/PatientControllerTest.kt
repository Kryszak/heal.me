package net.kryszak.healme.patient.port

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import java.util.UUID
import net.kryszak.healme.authentication.TenantId
import net.kryszak.healme.patient.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.*


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
        should("create new patient") {
            //given
            val request = CreatePatientDto("Jan", "Random", "random address")

            //when & then
            mockMvc.post("/patients") {
                header("x-api-key", "test")
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andExpect {
                status { isCreated() }
            }
        }

        should("return 403 response if tenant is not found") {
            //when & then
            mockMvc.get("/patients") {
                header("x-api-key", "i-do-not-exist")
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
                    TenantId(UUID.fromString("1bfcfd37-eafa-414a-94be-b377c7399a39"))
                )
            )

            //when & then
            mockMvc.get("/patients") {
                header("x-api-key", "test")
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
                    TenantId(UUID.fromString("1bfcfd37-eafa-414a-94be-b377c7399a39"))
                )
            )
                .map(Patient::id)
                .getOrNull()

            //when & then
            mockMvc.get("/patients/$patientId") {
                header("x-api-key", "test")
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
            mockMvc.get("/patients/10000") {
                header("x-api-key", "test")
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
                    TenantId(UUID.fromString("1bfcfd37-eafa-414a-94be-b377c7399a39"))
                )
            )
                .map(Patient::id)
                .getOrNull() ?: throw Exception()
            val request = UpdatePatientDto(patientId, "New name", "New Surname", "another address")

            //when & then
            mockMvc.put("/patients/$patientId") {
                header("x-api-key", "test")
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath("\$.name") { value("New name") } }
                content { jsonPath("\$.surname") { value("New Surname") } }
                content { jsonPath("\$.address") { value("another address") } }
            }
        }

        should("return 404 if attempted to update non existing patient") {
            //given
            val patientId = 10000L
            val request = UpdatePatientDto(patientId, "New name", "New Surname", "another address")

            //when & then
            mockMvc.put("/patients/$patientId") {
                header("x-api-key", "test")
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
                    TenantId(UUID.fromString("1bfcfd37-eafa-414a-94be-b377c7399a39"))
                )
            )
                .map(Patient::id)
                .getOrNull() ?: throw Exception()
            val request = UpdatePatientDto(2L, "New name", "New Surname", "another address")

            //when & then
            mockMvc.put("/patients/$patientId") {
                header("x-api-key", "test")
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
                    TenantId(UUID.fromString("1bfcfd37-eafa-414a-94be-b377c7399a39"))
                )
            )
                .map(Patient::id)
                .getOrNull()

            //when & then
            mockMvc.delete("/patients/$patientId") {
                header("x-api-key", "test")
            }.andExpect {
                status { isNoContent() }
            }
        }

        should("return 404 if attempted to delete not existing patient") {
            //when & then
            mockMvc.delete("/patients/10000") {
                header("x-api-key", "test")
            }.andExpect {
                status { isNotFound() }
            }
        }
    }
}
