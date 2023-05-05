package io.github.kryszak.healme.common.configuration

import io.github.kryszak.healme.authentication.port.AuthenticationFacade
import io.github.kryszak.healme.common.adapter.ContextTenantStore
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TenantConfiguration {

    @Bean
    fun commonTenantStore(request: HttpServletRequest, authenticationFacade: AuthenticationFacade) =
        ContextTenantStore(request, authenticationFacade)
}