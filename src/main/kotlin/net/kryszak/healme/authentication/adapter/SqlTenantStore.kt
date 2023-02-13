package net.kryszak.healme.authentication.adapter

import arrow.core.Either
import net.kryszak.healme.authentication.TenantId
import net.kryszak.healme.authentication.TenantStore
import org.springframework.security.authentication.BadCredentialsException

class SqlTenantStore(private val tenantRepository: TenantRepository) : TenantStore {

    override fun findTenant(apiKey: String): Either<Throwable, TenantId> =
        Either.catch {
            tenantRepository.findByApiKey(apiKey)
                ?: throw BadCredentialsException("Tenant with Api Key = {$apiKey} not found.")
        }.map(TenantEntity::toDomain)
}