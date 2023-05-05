package io.github.kryszak.healme.authentication.configuration

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter

class ApiKeyRequestFilter : AbstractPreAuthenticatedProcessingFilter() {
    override fun getPreAuthenticatedPrincipal(request: HttpServletRequest): Any =
        request.getHeader(AuthenticationConfiguration.AUTH_HEADER_NAME) ?: ""

    override fun getPreAuthenticatedCredentials(request: HttpServletRequest): Any? = null
}