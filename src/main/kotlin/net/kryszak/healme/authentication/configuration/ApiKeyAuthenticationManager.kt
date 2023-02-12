package net.kryszak.healme.authentication.configuration

import mu.KotlinLogging
import net.kryszak.healme.authentication.GetTenantQuery
import net.kryszak.healme.authentication.GetTenantQuery.Input
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication

class ApiKeyAuthenticationManager(private val getTenantQuery: GetTenantQuery) : AuthenticationManager {

    private val logger = KotlinLogging.logger {}

    override fun authenticate(authentication: Authentication): Authentication {
        val apiKey = authentication.principal as String

        logger.debug { "Authenticating Api Key: $apiKey" }

        if (apiKey.isBlank()) {
            throw BadCredentialsException("No authentication provided for request.")
        }

        getTenantQuery.execute(Input(apiKey))
            .onLeft {
                logger.debug { "No tenant found" }
                throw BadCredentialsException("No authentication provided for request.")
            }
            .onRight {
                logger.debug { "Tenant found : $it" }
                authentication.isAuthenticated = true
            }

        return authentication
    }
}