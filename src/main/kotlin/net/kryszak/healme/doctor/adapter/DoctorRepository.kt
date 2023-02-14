package net.kryszak.healme.doctor.adapter

import java.util.UUID
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

interface DoctorRepository : PagingAndSortingRepository<DoctorEntity, Long> {

    fun findAllByOwner(owner: UUID, pageable: Pageable): Page<DoctorEntity>

    fun findByIdAndOwner(id: Long, owner: UUID): DoctorEntity?
}