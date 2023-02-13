package net.kryszak.healme.patient.port

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import java.util.UUID
import net.kryszak.healme.authentication.TenantId
import net.kryszak.healme.patient.*
import net.kryszak.healme.patient.adapter.SqlPatientStore
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Sql(scripts = ["classpath:test_db/tenant.sql"])
class PatientControllerTest(
    mockMvc: MockMvc,
    objectMapper: ObjectMapper,
    patientStore: SqlPatientStore
) : ShouldSpec({

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
            content { jsonPath("\$.content[0].name") { value(PATIENT_NAME) } }
            content { jsonPath("\$.content[0].surname") { value(PATIENT_SURNAME) } }
            content { jsonPath("\$.content[0].address") { value(PATIENT_ADDRESS) } }
        }
    }

}) {
    override fun extensions() = listOf(SpringExtension)
}
