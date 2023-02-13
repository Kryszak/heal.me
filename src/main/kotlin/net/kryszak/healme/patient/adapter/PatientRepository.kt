package net.kryszak.healme.patient.adapter

import java.util.UUID
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

interface PatientRepository : PagingAndSortingRepository<PatientEntity, Long> {

    fun findAllByOwner(owner: UUID, pageable: Pageable): Page<PatientEntity>

    fun findByIdAndOwner(id: Long, owner: UUID): PatientEntity?
}