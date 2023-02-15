package net.kryszak.healme.visit.adapter

import java.time.LocalDateTime
import net.kryszak.healme.doctor.adapter.DoctorEntity
import net.kryszak.healme.patient.adapter.PatientEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository

interface VisitRepository : PagingAndSortingRepository<VisitEntity, Long> {

    @Query("select case when count(v) > 0 then true else false end from VisitEntity v where (v.patient = :patient or v.doctor = :doctor) and v.dateTime between :startTime and :endTime")
    fun existsInGivenTimeFrame(
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        patient: PatientEntity,
        doctor: DoctorEntity
    ): Boolean
}