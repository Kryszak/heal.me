package net.kryszak.healme.patient.configuration

import net.kryszak.healme.common.TenantStore
import net.kryszak.healme.patient.*
import net.kryszak.healme.patient.adapter.PatientRepository
import net.kryszak.healme.patient.adapter.SqlPatientStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PatientConfiguration {

    @Bean
    fun patientStore(patientRepository: PatientRepository): PatientStore = SqlPatientStore(patientRepository)

    @Bean
    fun createPatientCommand(patientStore: PatientStore, commonTenantStore: TenantStore) =
        CreatePatientCommand(patientStore, commonTenantStore)

    @Bean
    fun getPatientsQuery(patientStore: PatientStore, commonTenantStore: TenantStore) =
        GetPatientsQuery(patientStore, commonTenantStore)

    @Bean
    fun getPatientQuery(patientStore: PatientStore, commonTenantStore: TenantStore) =
        GetPatientQuery(patientStore, commonTenantStore)

    @Bean
    fun updatePatientCommand(patientStore: PatientStore, commonTenantStore: TenantStore) =
        UpdatePatientCommand(patientStore, commonTenantStore)

    @Bean
    fun deletePatientCommand(patientStore: PatientStore, commonTenantStore: TenantStore) =
        DeletePatientCommand(patientStore, commonTenantStore)
}