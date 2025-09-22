package io.github.kryszak.healme.visit

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.raise.either
import io.github.kryszak.healme.common.TenantStore
import java.time.Duration
import java.time.LocalDateTime

class CreateVisitCommand(
    private val doctorStore: DoctorStore,
    private val patientStore: PatientStore,
    private val visitStore: VisitStore,
    private val tenantStore: TenantStore,
    private val visitDuration: Duration,
) {

    fun execute(input: Input): Either<Throwable, Long> =
        either {
            CreateVisitParams(
                input.dateTime,
                input.place,
                doctorStore.getDoctor(input.doctorId).bind(),
                patientStore.getPatient(input.patientId).bind(),
                tenantStore.getCurrentTenant().bind()
            )
        }
            .flatMap(::validateVisitTimeAvailable)
            .flatMap(visitStore::saveVisit)
            .map { it.id }

    private fun validateVisitTimeAvailable(createVisitParams: CreateVisitParams): Either<Throwable, CreateVisitParams> =
        visitStore.existsVisitInGivenTimeWindow(
            ExistsVisitInTimeWindowParams(
                createVisitParams.dateTime.minus(visitDuration),
                createVisitParams.dateTime.plus(visitDuration),
                createVisitParams.patient,
                createVisitParams.doctor
            )
        ).flatMap {
            when (it) {
                true -> Either.Left(VisitTimeAlreadyTakenException("Visit for given time frame with doctor or patient already exists."))
                false -> Either.Right(createVisitParams)
            }
        }

    data class Input(
        val doctorId: Long,
        val patientId: Long,
        val dateTime: LocalDateTime,
        val place: String,
    )
}