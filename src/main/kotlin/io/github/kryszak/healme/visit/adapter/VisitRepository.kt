package io.github.kryszak.healme.visit.adapter

import io.github.kryszak.healme.doctor.adapter.DoctorEntity
import io.github.kryszak.healme.patient.adapter.PatientEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import java.time.LocalDateTime
import java.util.*

interface VisitRepository : PagingAndSortingRepository<VisitEntity, Long> {

    fun findByIdAndOwner(id: Long, owner: UUID): VisitEntity?

    @Query(
        "select case when count(v) > 0 then true else false end from VisitEntity v " +
                "where (v.patient = :patient or v.doctor = :doctor) and v.dateTime between :startTime and :endTime"
    )
    fun existsInGivenTimeFrame(
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        patient: PatientEntity,
        doctor: DoctorEntity,
    ): Boolean

    fun deleteByPatientIdAndOwner(patientId: Long, owner: UUID)

    fun deleteByDoctorIdAndOwner(doctorId: Long, owner: UUID)

    fun findAllByOwner(owner: UUID, pageable: Pageable): Page<VisitEntity>

    fun findAllByPatientIdAndOwner(patientId: Long, owner: UUID, pageable: Pageable): Page<VisitEntity>

    fun save(it: VisitEntity): VisitEntity

    fun delete(fromDomain: VisitEntity)
}