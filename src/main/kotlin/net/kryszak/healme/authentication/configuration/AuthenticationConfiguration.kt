package net.kryszak.healme.authentication.configuration

import net.kryszak.healme.authentication.adapter.SqlTenantStore
import net.kryszak.healme.authentication.adapter.TenantRepository
import net.kryszak.healme.authentication.GetTenantQuery
import net.kryszak.healme.authentication.TenantStore
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
        const val AUTH_HEADER_NAME = "X-Api-Key"
    }

    @Bean
    fun tenantStore(tenantRepository: TenantRepository) = SqlTenantStore(tenantRepository)

    @Bean
    fun getTenantQuery(tenantStore: TenantStore) = GetTenantQuery(tenantStore)

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
        http.antMatcher("/**")
            .csrf()
            .disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilter(apiKeyRequestFilter)
            .authorizeRequests()
            .anyRequest()
            .authenticated()
            .and()

        return http.build()
    }

}