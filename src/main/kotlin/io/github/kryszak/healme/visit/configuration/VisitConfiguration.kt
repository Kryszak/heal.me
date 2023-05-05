package io.github.kryszak.healme.visit.configuration

import io.github.kryszak.healme.common.TenantStore
import io.github.kryszak.healme.common.exception.ExceptionMapper
import io.github.kryszak.healme.doctor.port.DoctorFacade
import io.github.kryszak.healme.patient.port.PatientFacade
import io.github.kryszak.healme.visit.*
import io.github.kryszak.healme.visit.adapter.LocalDoctorStore
import io.github.kryszak.healme.visit.adapter.LocalPatientStore
import io.github.kryszak.healme.visit.adapter.SqlVisitStore
import io.github.kryszak.healme.visit.adapter.VisitRepository
import io.github.kryszak.healme.visit.port.VisitExceptionMapper
import io.github.kryszak.healme.visit.port.VisitFacade
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class VisitConfiguration {

    @Bean
    fun visitExceptionMapper(exceptionMapper: ExceptionMapper): VisitExceptionMapper {
        val visitExceptionMapper = VisitExceptionMapper(exceptionMapper)
        return visitExceptionMapper
    }

    @Bean
    fun visitStore(visitRepository: VisitRepository): VisitStore = SqlVisitStore(visitRepository)

    @Bean
    fun localDoctorStore(doctorFacade: DoctorFacade): DoctorStore = LocalDoctorStore(doctorFacade)

    @Bean
    fun localPatientStore(patientFacade: PatientFacade): PatientStore = LocalPatientStore(patientFacade)

    @Bean
    fun createVisitCommand(
        localDoctorStore: DoctorStore,
        localPatientStore: PatientStore,
        visitStore: VisitStore,
        commonTenantStore: TenantStore,
        @Value("\${healme.visit-duration}") visitDuration: Duration
    ) = CreateVisitCommand(localDoctorStore, localPatientStore, visitStore, commonTenantStore, visitDuration)

    @Bean
    fun deleteVisitsCommand(visitStore: VisitStore, commonTenantStore: TenantStore) =
        DeleteVisitsCommand(visitStore, commonTenantStore)

    @Bean
    fun deleteVisitCommand(visitStore: VisitStore, commonTenantStore: TenantStore) =
        DeleteVisitCommand(visitStore, commonTenantStore)

    @Bean
    fun getVisitsQuery(visitStore: VisitStore, commonTenantStore: TenantStore) =
        GetVisitsQuery(visitStore, commonTenantStore)

    @Bean
    fun getPatientVisitsQuery(visitStore: VisitStore, commonTenantStore: TenantStore) =
        GetPatientVisitsQuery(visitStore, commonTenantStore)

    @Bean
    fun updateVisitTimeCommand(
        visitStore: VisitStore,
        commonTenantStore: TenantStore,
        @Value("\${healme.visit-duration}") visitDuration: Duration
    ) = UpdateVisitTimeCommand(visitStore, commonTenantStore, visitDuration)

    @Bean
    fun visitFacade(deleteVisitsCommand: DeleteVisitsCommand) = VisitFacade(deleteVisitsCommand)
}