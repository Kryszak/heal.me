package net.kryszak.healme.visit.configuration

import java.time.Duration
import net.kryszak.healme.common.TenantStore
import net.kryszak.healme.common.exception.ExceptionMapper
import net.kryszak.healme.doctor.port.DoctorFacade
import net.kryszak.healme.patient.port.PatientFacade
import net.kryszak.healme.visit.*
import net.kryszak.healme.visit.adapter.LocalDoctorStore
import net.kryszak.healme.visit.adapter.LocalPatientStore
import net.kryszak.healme.visit.adapter.SqlVisitStore
import net.kryszak.healme.visit.adapter.VisitRepository
import net.kryszak.healme.visit.port.VisitExceptionMapper
import net.kryszak.healme.visit.port.VisitFacade
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class VisitConfiguration {

    @Bean
    fun visitExceptionMapper(exceptionMapper: ExceptionMapper) = VisitExceptionMapper(exceptionMapper)

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
    fun visitFacade(deleteVisitsCommand: DeleteVisitsCommand) = VisitFacade(deleteVisitsCommand)
}