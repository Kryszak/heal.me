package net.kryszak.healme.authentication.configuration

import net.kryszak.healme.authentication.GetTenantQuery
import net.kryszak.healme.authentication.TenantStore
import net.kryszak.healme.authentication.adapter.SqlTenantStore
import net.kryszak.healme.authentication.adapter.TenantRepository
import net.kryszak.healme.authentication.port.AuthenticationFacade
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class AuthenticationConfiguration {
    companion object {
        const val AUTH_HEADER_NAME = "x-api-key"
    }

    @Bean
    fun tenantStore(tenantRepository: TenantRepository) = SqlTenantStore(tenantRepository)

    @Bean
    fun getTenantQuery(tenantStore: TenantStore) = GetTenantQuery(tenantStore)

    @Bean
    fun authenticationFacade(getTenantQuery: GetTenantQuery) = AuthenticationFacade(getTenantQuery)

    @Bean
    fun apiKeyAuthenticationManager(getTenantQuery: GetTenantQuery) = ApiKeyAuthenticationManager(getTenantQuery)

    @Bean
    fun apiKeyRequestFilter(apiKeyAuthenticationManager: ApiKeyAuthenticationManager): ApiKeyRequestFilter {
        val apiKeyRequestFilter = ApiKeyRequestFilter()
        apiKeyRequestFilter.setAuthenticationManager(apiKeyAuthenticationManager)
        return apiKeyRequestFilter
    }

    @Bean
    fun filterChain(http: HttpSecurity, apiKeyRequestFilter: ApiKeyRequestFilter): SecurityFilterChain {
        http
            .csrf()
            .disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilter(apiKeyRequestFilter)
            .authorizeHttpRequests()
            .anyRequest()
            .authenticated()

        return http.build()
    }

}