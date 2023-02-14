package net.kryszak.healme.doctor.port

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import java.util.UUID
import net.kryszak.healme.authentication.TenantId
import net.kryszak.healme.doctor.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Sql(scripts = ["classpath:test_db/tenant.sql"])
class DoctorControllerTest : ShouldSpec() {
    override fun extensions() = listOf(SpringExtension)

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var doctorStore: DoctorStore

    init {
        should("create new doctor") {
            //given
            val request = CreateDoctorDto("Jan", "Random", "Chirurg")

            //when & then
            mockMvc.post("/doctors") {
                header("x-api-key", "test")
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andExpect {
                status { isCreated() }
            }
        }

        should("return 403 response if tenant is not found") {
            //when & then
            mockMvc.get("/doctors") {
                header("x-api-key", "i-do-not-exist")
            }.andExpect {
                status { isForbidden() }
            }
        }

        should("retrieve doctor list") {
            //given
            doctorStore.saveDoctor(
                CreateDoctorParams(
                    DOCTOR_NAME,
                    DOCTOR_SURNAME,
                    DOCTOR_SPECIALIZATION,
                    TenantId(UUID.fromString("1bfcfd37-eafa-414a-94be-b377c7399a39"))
                )
            )

            //when & then
            mockMvc.get("/doctors") {
                header("x-api-key", "test")
            }.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath("\$.content[0].name") { isNotEmpty() } }
                content { jsonPath("\$.content[0].surname") { isNotEmpty() } }
                content { jsonPath("\$.content[0].specialization") { isNotEmpty() } }
            }
        }

        should("retrieve doctor for given id") {
            //given
            val doctorId = doctorStore.saveDoctor(
                CreateDoctorParams(
                    DOCTOR_NAME,
                    DOCTOR_SURNAME,
                    DOCTOR_SPECIALIZATION,
                    TenantId(UUID.fromString("1bfcfd37-eafa-414a-94be-b377c7399a39"))
                )
            )
                .map(Doctor::id)
                .getOrNull()

            //when & then
            mockMvc.get("/doctors/$doctorId") {
                header("x-api-key", "test")
            }.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath("\$.name") { value(DOCTOR_NAME) } }
                content { jsonPath("\$.surname") { value(DOCTOR_SURNAME) } }
                content { jsonPath("\$.specialization") { value(DOCTOR_SPECIALIZATION) } }
            }
        }

        should("return 404 status if doctor was not found") {
            //when & then
            mockMvc.get("/doctors/10000") {
                header("x-api-key", "test")
            }.andExpect {
                status { isNotFound() }
            }
        }

        should("update doctor") {
            //given
            val doctorId = doctorStore.saveDoctor(
                CreateDoctorParams(
                    DOCTOR_NAME,
                    DOCTOR_SURNAME,
                    DOCTOR_SPECIALIZATION,
                    TenantId(UUID.fromString("1bfcfd37-eafa-414a-94be-b377c7399a39"))
                )
            )
                .map(Doctor::id)
                .getOrNull() ?: throw Exception()
            val request = UpdateDoctorDto(doctorId, "New name", "New Surname", "another specialization")

            //when & then
            mockMvc.put("/doctors/$doctorId") {
                header("x-api-key", "test")
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath("\$.name") { value("New name") } }
                content { jsonPath("\$.surname") { value("New Surname") } }
                content { jsonPath("\$.specialization") { value("another specialization") } }
            }
        }

        should("return 404 if attempted to update non existing doctor") {
            //given
            val doctorId = 10000L
            val request = UpdateDoctorDto(doctorId, "New name", "New Surname", "another address")

            //when & then
            mockMvc.put("/doctors/$doctorId") {
                header("x-api-key", "test")
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andExpect {
                status { isNotFound() }
            }
        }

        should("return 400 if attempted to update doctor with id other than provided in url") {
            //given
            val doctorId = doctorStore.saveDoctor(
                CreateDoctorParams(
                    DOCTOR_NAME,
                    DOCTOR_SURNAME,
                    DOCTOR_SPECIALIZATION,
                    TenantId(UUID.fromString("1bfcfd37-eafa-414a-94be-b377c7399a39"))
                )
            )
                .map(Doctor::id)
                .getOrNull() ?: throw Exception()
            val request = UpdateDoctorDto(2L, "New name", "New Surname", "another address")

            //when & then
            mockMvc.put("/doctors/$doctorId") {
                header("x-api-key", "test")
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andExpect {
                status { isBadRequest() }
            }
        }
    }
}