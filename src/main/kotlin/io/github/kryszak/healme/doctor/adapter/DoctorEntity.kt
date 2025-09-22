package io.github.kryszak.healme.doctor.adapter

import io.github.kryszak.healme.authentication.TenantId
import io.github.kryszak.healme.doctor.CreateDoctorParams
import io.github.kryszak.healme.doctor.Doctor
import jakarta.persistence.*
import org.hibernate.Hibernate
import java.util.*

@Entity
@Table(name = "doctor")
class DoctorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    lateinit var name: String

    lateinit var surname: String

    lateinit var specialization: String

    lateinit var owner: UUID

    fun toDomain() = Doctor(id, name, surname, specialization, TenantId(owner))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as DoctorEntity

        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {
        fun fromParams(params: CreateDoctorParams) = DoctorEntity().apply {
            name = params.name
            surname = params.surname
            specialization = params.specialization
            owner = params.owner.value
        }

        fun fromDomain(doctor: Doctor) = DoctorEntity().apply {
            id = doctor.id
            name = doctor.name
            surname = doctor.surname
            specialization = doctor.specialization
            owner = doctor.owner.value
        }
    }
}