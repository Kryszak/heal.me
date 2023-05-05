package io.github.kryszak.healme.authentication.adapter

import arrow.core.Either
import io.github.kryszak.healme.authentication.TenantStore
import io.github.kryszak.healme.common.exception.DataNotFoundException

class SqlTenantStore(private val tenantRepository: TenantRepository) :
    TenantStore {

    override fun findTenant(apiKey: String): Either<Throwable, io.github.kryszak.healme.authentication.TenantId> =
        Either.catch {
            tenantRepository.findByApiKey(apiKey)
                ?: throw DataNotFoundException("Tenant with Api Key = {$apiKey} not found.")
        }.map(TenantEntity::toDomain)
}