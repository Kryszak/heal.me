package net.kryszak.healme.doctor.configuration

import net.kryszak.healme.common.TenantStore
import net.kryszak.healme.doctor.*
import net.kryszak.healme.doctor.adapter.DoctorRepository
import net.kryszak.healme.doctor.adapter.SqlDoctorStore
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
}