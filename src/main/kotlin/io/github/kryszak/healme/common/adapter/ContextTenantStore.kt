package io.github.kryszak.healme.common.adapter

import arrow.core.Either
import io.github.kryszak.healme.authentication.TenantId
import io.github.kryszak.healme.authentication.port.AuthenticationFacade
import io.github.kryszak.healme.common.TenantStore
import jakarta.servlet.http.HttpServletRequest

class ContextTenantStore(
    private val request: HttpServletRequest,
    private val authenticationFacade: AuthenticationFacade
) : TenantStore {
    override fun getCurrentTenant(): Either<Throwable, TenantId> =
        request.getHeader("x-api-key")
            .let(authenticationFacade::getTenant)
}