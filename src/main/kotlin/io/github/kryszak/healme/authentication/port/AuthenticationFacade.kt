package io.github.kryszak.healme.authentication.port

import arrow.core.Either
import io.github.kryszak.healme.authentication.GetTenantQuery
import io.github.kryszak.healme.authentication.GetTenantQuery.Input
import io.github.kryszak.healme.authentication.TenantId

class AuthenticationFacade(private val getTenantQuery: GetTenantQuery) {

    fun getTenant(apiKey: String): Either<Throwable, TenantId> =
        getTenantQuery.execute(Input(apiKey))
}