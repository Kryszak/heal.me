package net.kryszak.healme.authentication.port

import arrow.core.Either
import net.kryszak.healme.authentication.GetTenantQuery
import net.kryszak.healme.authentication.TenantId

class AuthenticationFacade(private val getTenantQuery: GetTenantQuery) {

    fun getTenant(apiKey: String): Either<Throwable, TenantId> =
        getTenantQuery.execute(GetTenantQuery.Input(apiKey))
}