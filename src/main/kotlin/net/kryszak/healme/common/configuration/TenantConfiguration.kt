package net.kryszak.healme.common.configuration

import javax.servlet.http.HttpServletRequest
import net.kryszak.healme.authentication.port.AuthenticationFacade
import net.kryszak.healme.common.adapter.ContextTenantStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TenantConfiguration {

    @Bean
    fun commonTenantStore(request: HttpServletRequest, authenticationFacade: AuthenticationFacade) =
        ContextTenantStore(request, authenticationFacade)
}