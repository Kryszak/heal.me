package net.kryszak.healme.doctor.port

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import net.kryszak.healme.authentication.*
import java.util.UUID
import net.kryszak.healme.doctor.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.*

private const val s = "New Surname"

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
        val doctorsUrl = "/doctors"

        val updatedDoctorName = "New name"
        val updatedDoctorSurname = "New Surname"
        val updatedDoctorSpecialization = "another specialization"

        should("create new doctor") {
            //given
            val request = CreateDoctorDto("Jan", "Random", "Chirurg")

            //when & then
            mockMvc.post(doctorsUrl) {
                header(API_KEY_HEADER, VALID_API_KEY)
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andExpect {
                status { isCreated() }
            }
        }

        should("return 403 response if tenant is not found") {
            //when & then
            mockMvc.get(doctorsUrl) {
                header(API_KEY_HEADER, INVALID_API_KEY)
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
                    TENANT_ID
                )
            )

            //when & then
            mockMvc.get(doctorsUrl) {
                header(API_KEY_HEADER, VALID_API_KEY)
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
                    TENANT_ID
                )
            )
                .map(Doctor::id)
                .getOrNull()

            //when & then
            mockMvc.get("$doctorsUrl/$doctorId") {
                header(API_KEY_HEADER, VALID_API_KEY)
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
            mockMvc.get("$doctorsUrl/10000") {
                header(API_KEY_HEADER, VALID_API_KEY)
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
                    TENANT_ID
                )
            )
                .map(Doctor::id)
                .getOrNull() ?: throw Exception()
            val request = UpdateDoctorDto(doctorId, updatedDoctorName, updatedDoctorSurname,
                updatedDoctorSpecialization
            )

            //when & then
            mockMvc.put("$doctorsUrl/$doctorId") {
                header(API_KEY_HEADER, VALID_API_KEY)
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { jsonPath("\$.name") { value(updatedDoctorName) } }
                content { jsonPath("\$.surname") { value(updatedDoctorSurname) } }
                content { jsonPath("\$.specialization") { value(updatedDoctorSpecialization) } }
            }
        }

        should("return 404 if attempted to update non existing doctor") {
            //given
            val doctorId = 10000L
            val request = UpdateDoctorDto(doctorId, updatedDoctorName, updatedDoctorSurname, updatedDoctorSpecialization)

            //when & then
            mockMvc.put("$doctorsUrl/$doctorId") {
                header(API_KEY_HEADER, VALID_API_KEY)
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
                    TENANT_ID
                )
            )
                .map(Doctor::id)
                .getOrNull() ?: throw Exception()
            val request = UpdateDoctorDto(2L, updatedDoctorName, updatedDoctorSurname, updatedDoctorSpecialization)

            //when & then
            mockMvc.put("$doctorsUrl/$doctorId") {
                header(API_KEY_HEADER, VALID_API_KEY)
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andExpect {
                status { isBadRequest() }
            }
        }

        should("delete given doctor") {
            //given
            val doctorId = doctorStore.saveDoctor(
                CreateDoctorParams(
                    DOCTOR_NAME,
                    DOCTOR_SURNAME,
                    DOCTOR_SPECIALIZATION,
                    TENANT_ID
                )
            )
                .map(Doctor::id)
                .getOrNull()

            //when & then
            mockMvc.delete("$doctorsUrl/$doctorId") {
                header(API_KEY_HEADER, VALID_API_KEY)
            }.andExpect {
                status { isNoContent() }
            }
        }

        should("return 404 if attempted to delete not existing doctor") {
            //when & then
            mockMvc.delete("$doctorsUrl/10000") {
                header(API_KEY_HEADER, VALID_API_KEY)
            }.andExpect {
                status { isNotFound() }
            }
        }
    }
}