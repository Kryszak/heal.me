package net.kryszak.healme.patient.port

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Sql(scripts = ["classpath:test-data.sql"])
class PatientControllerTest(mockMvc: MockMvc, objectMapper: ObjectMapper) : ShouldSpec({

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

}) {
    override fun extensions() = listOf(SpringExtension)
}
