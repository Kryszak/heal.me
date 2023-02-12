package net.kryszak.healme.authentication.adapter

import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import net.kryszak.healme.authentication.TenantId

@Entity
@Table(name = "tenant")
class TenantEntity {

    @Id
    lateinit var id: UUID

    lateinit var apiKey: String

    fun toDomain() = TenantId(id)
}