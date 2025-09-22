package io.github.kryszak.healme

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.kryszak.healme.doctor.DoctorStore
import io.github.kryszak.healme.patient.PatientStore
import io.github.kryszak.healme.visit.VisitStore
import io.kotest.core.spec.style.ShouldSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class BaseIntegrationTest : ShouldSpec() {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var patientStore: PatientStore

    @Autowired
    lateinit var doctorStore: DoctorStore

    @Autowired
    lateinit var visitStore: VisitStore
}