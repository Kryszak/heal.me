package net.kryszak.healme.authentication.adapter

import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import net.kryszak.healme.authentication.TenantId
import org.hibernate.Hibernate

@Entity
@Table(name = "tenant")
class TenantEntity {

    @Id
    lateinit var id: UUID

    lateinit var apiKey: String

    fun toDomain() = TenantId(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as TenantEntity

        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}