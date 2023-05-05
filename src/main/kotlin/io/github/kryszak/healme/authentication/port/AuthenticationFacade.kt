package io.github.kryszak.healme.authentication.port

import arrow.core.Either

class AuthenticationFacade(private val getTenantQuery: io.github.kryszak.healme.authentication.GetTenantQuery) {

    fun getTenant(apiKey: String): Either<Throwable, io.github.kryszak.healme.authentication.TenantId> =
        getTenantQuery.execute(io.github.kryszak.healme.authentication.GetTenantQuery.Input(apiKey))
}