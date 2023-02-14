package net.kryszak.healme.doctor.adapter

import arrow.core.Either
import net.kryszak.healme.authentication.TenantId
import net.kryszak.healme.common.exception.DataNotFoundException
import net.kryszak.healme.doctor.CreateDoctorParams
import net.kryszak.healme.doctor.Doctor
import net.kryszak.healme.doctor.DoctorStore
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class SqlDoctorStore(
    private val doctorRepository: DoctorRepository
) : DoctorStore {
    override fun saveDoctor(params: CreateDoctorParams): Either<Throwable, Doctor> =
        DoctorEntity.fromParams(params)
            .let { Either.catch { doctorRepository.save(it) } }
            .map(DoctorEntity::toDomain)

    override fun findDoctors(tenantId: TenantId, pageable: Pageable): Either<Throwable, Page<Doctor>> =
        Either.catch { doctorRepository.findAllByOwner(tenantId.value, pageable) }
            .map { it.map(DoctorEntity::toDomain) }

    override fun findDoctor(tenantId: TenantId, doctorId: Long): Either<Throwable, Doctor> =
        Either.catch {
            doctorRepository.findByIdAndOwner(doctorId, tenantId.value)
                ?: throw DataNotFoundException("Doctor with id={$doctorId} not found under owner={$tenantId}")
        }.map(DoctorEntity::toDomain)

}