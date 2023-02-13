package net.kryszak.healme.authentication

import arrow.core.Either
import org.springframework.cache.annotation.Cacheable

open class GetTenantQuery(private val tenantStore: TenantStore) {

    @Cacheable("tenants")
    open fun execute(input: Input): Either<Throwable, TenantId> =
        tenantStore.findTenant(input.apiKey)

    data class Input(val apiKey: String)
}