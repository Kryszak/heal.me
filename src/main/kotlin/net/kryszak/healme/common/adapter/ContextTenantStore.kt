package net.kryszak.healme.common.adapter

import arrow.core.Either
import javax.servlet.http.HttpServletRequest
import net.kryszak.healme.authentication.GetTenantQuery
import net.kryszak.healme.authentication.TenantId
import net.kryszak.healme.common.TenantStore

class ContextTenantStore(
    private val request: HttpServletRequest,
    private val getTenantQuery: GetTenantQuery
) : TenantStore {
    override fun getCurrentTenant(): Either<Throwable, TenantId> =
        request.getHeader("x-api-key")
            .let { getTenantQuery.execute(GetTenantQuery.Input(it)) }

}