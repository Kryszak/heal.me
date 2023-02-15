package net.kryszak.healme.visit.port

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import java.util.UUID
import net.kryszak.healme.authentication.TenantId
import net.kryszak.healme.doctor.*
import net.kryszak.healme.patient.*
import net.kryszak.healme.visit.VisitStore
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
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
class VisitControllerTest : ShouldSpec() {

    override fun extensions() = listOf(SpringExtension)

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
        should("create new visit") {
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
                .getOrNull()!!

            val doctorId = doctorStore.saveDoctor(
                CreateDoctorParams(
                    DOCTOR_NAME,
                    DOCTOR_SURNAME,
                    DOCTOR_SPECIALIZATION,
                    TenantId(UUID.fromString("1bfcfd37-eafa-414a-94be-b377c7399a39"))
                )
            )
                .map(Doctor::id)
                .getOrNull()!!

            val request = CreateVisitDto("2023-02-01", "15:00", "Medical place", doctorId, patientId)

            //when & then
            mockMvc.post("/visits") {
                header("x-api-key", "test")
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andExpect {
                status { isCreated() }
            }
        }

        should("return 403 response if tenant is not found") {
            //when & then
            mockMvc.get("/visits") {
                header("x-api-key", "i-do-not-exist")
            }.andExpect {
                status { isForbidden() }
            }
        }
    }
}