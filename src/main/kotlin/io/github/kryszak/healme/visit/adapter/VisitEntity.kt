package io.github.kryszak.healme.visit.adapter

import io.github.kryszak.healme.authentication.TenantId
import io.github.kryszak.healme.doctor.adapter.DoctorEntity
import io.github.kryszak.healme.patient.adapter.PatientEntity
import io.github.kryszak.healme.visit.CreateVisitParams
import io.github.kryszak.healme.visit.Visit
import jakarta.persistence.*
import org.hibernate.Hibernate
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "visit")
class VisitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    lateinit var dateTime: LocalDateTime

    lateinit var place: String

    // NOTE
    // THis two relations should be mapped using dedicated view-only mapping classes,
    // not db models from another domain packages.
    // This approach was picked to reduce code duplication and speed up development process.
    @OneToOne
    @JoinColumn(name = "doctor_id")
    lateinit var doctor: DoctorEntity

    @OneToOne
    @JoinColumn(name = "patient_id")
    lateinit var patient: PatientEntity

    lateinit var owner: UUID

    fun toDomain() = Visit(
        id, dateTime, place, doctor.toDomain(), patient.toDomain(),
        TenantId(owner)
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as VisitEntity

        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {
        fun fromParams(params: CreateVisitParams) = VisitEntity().apply {
            dateTime = params.dateTime
            place = params.place
            doctor = DoctorEntity.fromDomain(params.doctor)
            patient = PatientEntity.fromDomain(params.patient)
            owner = params.owner.value
        }

        fun fromDomain(visit: Visit) = VisitEntity().apply {
            id = visit.id
            dateTime = visit.dateTime
            place = visit.place
            doctor = DoctorEntity.fromDomain(visit.doctor)
            patient = PatientEntity.fromDomain(visit.patient)
            owner = visit.owner.value
        }
    }
}
