package io.github.kryszak.healme.doctor.port

import io.github.kryszak.healme.BaseIntegrationTest
import io.github.kryszak.healme.authentication.API_KEY_HEADER
import io.github.kryszak.healme.authentication.INVALID_API_KEY
import io.github.kryszak.healme.authentication.TENANT_ID
import io.github.kryszak.healme.authentication.VALID_API_KEY
import io.github.kryszak.healme.doctor.*
import io.kotest.core.extensions.ApplyExtension
import io.kotest.extensions.spring.SpringExtension
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put


@ApplyExtension(SpringExtension::class)
class DoctorControllerTest : BaseIntegrationTest() {

    val doctorsUrl = "/doctors"

    fun formatDoctorUrl(doctorId: Long?) = "$doctorsUrl/$doctorId"

    init {
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
            mockMvc.get(formatDoctorUrl(doctorId)) {
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
            mockMvc.get(formatDoctorUrl(10000L)) {
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
                .getOrNull()!!
            val request = UpdateDoctorDto(
                doctorId, updatedDoctorName, updatedDoctorSurname,
                updatedDoctorSpecialization
            )

            //when & then
            mockMvc.put(formatDoctorUrl(doctorId)) {
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
            val request =
                UpdateDoctorDto(doctorId, updatedDoctorName, updatedDoctorSurname, updatedDoctorSpecialization)

            //when & then
            mockMvc.put(formatDoctorUrl(doctorId)) {
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
                .getOrNull()!!
            val request = UpdateDoctorDto(2L, updatedDoctorName, updatedDoctorSurname, updatedDoctorSpecialization)

            //when & then
            mockMvc.put(formatDoctorUrl(doctorId)) {
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
                .getOrNull()!!

            //when & then
            mockMvc.delete(formatDoctorUrl(doctorId)) {
                header(API_KEY_HEADER, VALID_API_KEY)
            }.andExpect {
                status { isNoContent() }
            }
        }

        should("return 404 if attempted to delete not existing doctor") {
            //when & then
            mockMvc.delete(formatDoctorUrl(10000L)) {
                header(API_KEY_HEADER, VALID_API_KEY)
            }.andExpect {
                status { isNotFound() }
            }
        }
    }

}