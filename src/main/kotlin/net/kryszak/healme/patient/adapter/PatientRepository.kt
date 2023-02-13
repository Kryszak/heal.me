package net.kryszak.healme.patient.adapter

import org.springframework.data.repository.PagingAndSortingRepository

interface PatientRepository : PagingAndSortingRepository<PatientEntity, Long>