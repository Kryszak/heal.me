package net.kryszak.healme.common.adapter

import arrow.core.Either
import jakarta.servlet.http.HttpServletRequest
import net.kryszak.healme.authentication.TenantId
import net.kryszak.healme.authentication.port.AuthenticationFacade
import net.kryszak.healme.common.TenantStore

class ContextTenantStore(
    private val request: HttpServletRequest,
    private val authenticationFacade: AuthenticationFacade
) : TenantStore {
    override fun getCurrentTenant(): Either<Throwable, TenantId> =
        request.getHeader("x-api-key")
            .let(authenticationFacade::getTenant)
}