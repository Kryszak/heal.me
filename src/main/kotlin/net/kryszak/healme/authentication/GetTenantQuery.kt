package net.kryszak.healme.authentication

import arrow.core.Either

class GetTenantQuery(private val tenantStore: TenantStore) {

    fun execute(input: Input): Either<Throwable, TenantId> =
        tenantStore.findTenant(input.apiKey)

    data class Input(val apiKey: String)
}