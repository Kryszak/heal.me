package net.kryszak.healme.common.configuration

import javax.servlet.http.HttpServletRequest
import net.kryszak.healme.authentication.GetTenantQuery
import net.kryszak.healme.common.adapter.ContextTenantStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TenantConfiguration {

    @Bean
    fun commonTenantStore(request: HttpServletRequest, getTenantQuery: GetTenantQuery) =
        ContextTenantStore(request, getTenantQuery)
}