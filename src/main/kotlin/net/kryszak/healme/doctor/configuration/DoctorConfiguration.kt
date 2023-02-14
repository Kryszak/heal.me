package net.kryszak.healme.doctor.configuration

import net.kryszak.healme.common.TenantStore
import net.kryszak.healme.doctor.CreateDoctorCommand
import net.kryszak.healme.doctor.DoctorStore
import net.kryszak.healme.doctor.GetDoctorQuery
import net.kryszak.healme.doctor.GetDoctorsQuery
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
}