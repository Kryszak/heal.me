package io.github.kryszak.healme.authentication.adapter

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface TenantRepository : JpaRepository<io.github.kryszak.healme.authentication.adapter.TenantEntity, UUID> {

    fun findByApiKey(apiKey: String): io.github.kryszak.healme.authentication.adapter.TenantEntity?
}