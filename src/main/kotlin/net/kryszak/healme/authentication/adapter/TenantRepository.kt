package net.kryszak.healme.authentication.adapter

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface TenantRepository : JpaRepository<TenantEntity, UUID> {

    fun findByApiKey(apiKey: String): TenantEntity?
}