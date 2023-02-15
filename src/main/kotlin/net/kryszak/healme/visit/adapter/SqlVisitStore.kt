package net.kryszak.healme.visit.adapter

import arrow.core.Either
import net.kryszak.healme.doctor.adapter.DoctorEntity
import net.kryszak.healme.patient.adapter.PatientEntity
import net.kryszak.healme.visit.CreateVisitParams
import net.kryszak.healme.visit.ExistsVisitInTimeWindowParams
import net.kryszak.healme.visit.Visit
import net.kryszak.healme.visit.VisitStore

class SqlVisitStore(private val visitRepository: VisitRepository) : VisitStore {
    override fun saveVisit(params: CreateVisitParams): Either<Throwable, Visit> =
        VisitEntity.fromParams(params)
            .let { Either.catch { visitRepository.save(it) } }
            .map(VisitEntity::toDomain)

    override fun existsVisitInGivenTimeWindow(params: ExistsVisitInTimeWindowParams): Either<Throwable, Boolean> =
        Either.catch {
            visitRepository.existsInGivenTimeFrame(
                params.startTime,
                params.endTime,
                PatientEntity.fromDomain(params.patient),
                DoctorEntity.fromDomain(params.doctor)
            )
        }

}