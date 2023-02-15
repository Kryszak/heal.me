package net.kryszak.healme.visit.adapter

import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.*
import net.kryszak.healme.authentication.TenantId
import net.kryszak.healme.doctor.adapter.DoctorEntity
import net.kryszak.healme.patient.adapter.PatientEntity
import net.kryszak.healme.visit.CreateVisitParams
import net.kryszak.healme.visit.Visit
import org.hibernate.Hibernate
import org.hibernate.annotations.Type

@Entity
@Table(name = "visit")
class VisitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    lateinit var dateTime: LocalDateTime

    lateinit var place: String

    @OneToOne
    @JoinColumn(name = "doctor_id")
    lateinit var doctor: DoctorEntity

    @OneToOne
    @JoinColumn(name = "patient_id")
    lateinit var patient: PatientEntity

    @Type(type = "uuid-char")
    lateinit var owner: UUID

    fun toDomain() = Visit(id, dateTime, place, doctor.toDomain(), patient.toDomain(), TenantId(owner))

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