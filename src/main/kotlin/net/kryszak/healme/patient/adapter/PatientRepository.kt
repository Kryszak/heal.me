package net.kryszak.healme.patient.adapter

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.*

interface PatientRepository : PagingAndSortingRepository<PatientEntity, Long> {

    fun findAllByOwner(owner: UUID, pageable: Pageable): Page<PatientEntity>

    fun findByIdAndOwner(id: Long, owner: UUID): PatientEntity?

    fun save(it: PatientEntity): PatientEntity
    
    fun delete(fromDomain: PatientEntity)
}