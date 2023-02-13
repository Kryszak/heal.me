package net.kryszak.healme.patient.adapter

import java.util.UUID
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import net.kryszak.healme.authentication.TenantId
import net.kryszak.healme.patient.CreatePatientParams
import net.kryszak.healme.patient.Patient
import org.hibernate.Hibernate
import org.hibernate.annotations.Type

@Entity
@Table(name = "patient")
class PatientEntity {

    @Id
    @GeneratedValue
    var id: Long = 0

    lateinit var name: String

    lateinit var surname: String

    lateinit var address: String

    @Type(type = "uuid-char")
    lateinit var owner: UUID

    fun toDomain() = Patient(id, name, surname, address, TenantId(owner))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as PatientEntity

        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {
        fun from(params: CreatePatientParams) = PatientEntity().apply {
            name = params.name
            surname = params.surname
            address = params.address
            owner = params.owner.value
        }
    }
}