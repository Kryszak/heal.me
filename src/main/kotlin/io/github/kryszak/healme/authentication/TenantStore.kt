package io.github.kryszak.healme.authentication

import arrow.core.Either

interface TenantStore {
    fun findTenant(apiKey: String): Either<Throwable, io.github.kryszak.healme.authentication.TenantId>
}