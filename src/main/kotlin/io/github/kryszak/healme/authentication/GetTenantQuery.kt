package io.github.kryszak.healme.authentication

import arrow.core.Either
import org.springframework.cache.annotation.Cacheable

open class GetTenantQuery(private val tenantStore: io.github.kryszak.healme.authentication.TenantStore) {

    // NOTE: caching mechanism should not be present in domain layer,
    // instead it should be done on port/adapter layer
    // This query breaks the rule just not to create another layer for the same functionality
    // with caching capabilities
    @Cacheable("tenants")
    open fun execute(input: io.github.kryszak.healme.authentication.GetTenantQuery.Input): Either<Throwable, io.github.kryszak.healme.authentication.TenantId> =
        tenantStore.findTenant(input.apiKey)

    data class Input(val apiKey: String)
}