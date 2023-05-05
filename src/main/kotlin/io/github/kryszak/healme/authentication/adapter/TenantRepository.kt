package io.github.kryszak.healme.authentication.adapter

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TenantRepository : JpaRepository<TenantEntity, UUID> {

    fun findByApiKey(apiKey: String): TenantEntity?
}