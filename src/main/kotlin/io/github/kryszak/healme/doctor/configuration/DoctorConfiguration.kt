package io.github.kryszak.healme.doctor.configuration

import io.github.kryszak.healme.common.TenantStore
import io.github.kryszak.healme.doctor.*
import io.github.kryszak.healme.doctor.adapter.DoctorRepository
import io.github.kryszak.healme.doctor.adapter.LocalVisitStore
import io.github.kryszak.healme.doctor.adapter.SqlDoctorStore
import io.github.kryszak.healme.doctor.port.DoctorFacade
import io.github.kryszak.healme.visit.port.VisitFacade
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DoctorConfiguration {

    @Bean
    fun doctorStore(doctorRepository: DoctorRepository): DoctorStore = SqlDoctorStore(doctorRepository)

    @Bean
    fun createDoctorCommand(doctorStore: DoctorStore, commonTenantStore: TenantStore) =
        CreateDoctorCommand(doctorStore, commonTenantStore)

    @Bean
    fun getDoctorsQuery(doctorStore: DoctorStore, commonTenantStore: TenantStore) =
        GetDoctorsQuery(doctorStore, commonTenantStore)

    @Bean
    fun getDoctorQuery(doctorStore: DoctorStore, commonTenantStore: TenantStore) =
        GetDoctorQuery(doctorStore, commonTenantStore)

    @Bean
    fun updateDoctorCommand(doctorStore: DoctorStore, commonTenantStore: TenantStore) =
        UpdateDoctorCommand(doctorStore, commonTenantStore)

    @Bean
    fun deleteDoctorCommand(doctorStore: DoctorStore, doctorVisitStore: VisitStore, commonTenantStore: TenantStore) =
        DeleteDoctorCommand(doctorStore, doctorVisitStore, commonTenantStore)

    @Bean
    fun doctorFacade(getDoctorQuery: GetDoctorQuery) = DoctorFacade(getDoctorQuery)

    @Bean
    fun doctorVisitStore(visitFacade: VisitFacade): VisitStore = LocalVisitStore(visitFacade)
}