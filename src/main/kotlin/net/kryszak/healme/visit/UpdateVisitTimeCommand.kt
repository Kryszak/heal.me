package net.kryszak.healme.visit

import arrow.core.Either
import arrow.core.flatMap
import java.time.Duration
import java.time.LocalTime
import net.kryszak.healme.common.TenantStore

class UpdateVisitTimeCommand(
    private val visitStore: VisitStore,
    private val tenantStore: TenantStore,
    private val visitDuration: Duration,
) {

    fun execute(input: Input): Either<Throwable, Unit> =
        tenantStore.getCurrentTenant()
            .flatMap { visitStore.findVisit(input.visitId, it) }
            .map { adjustVisitTime(it, input.time) }
            .flatMap(::validateVisitTimeAvailable)
            .flatMap(visitStore::updateVisit)
            .map { }

    private fun adjustVisitTime(visit: Visit, time: LocalTime): Visit =
        visit.copy(dateTime = visit.dateTime.with(time))

    private fun validateVisitTimeAvailable(visit: Visit): Either<Throwable, Visit> =
        visitStore.existsVisitInGivenTimeWindow(
            ExistsVisitInTimeWindowParams(
                visit.dateTime.minus(visitDuration),
                visit.dateTime.plus(visitDuration),
                visit.patient,
                visit.doctor
            )
        ).flatMap {
            when (it) {
                true -> Either.Left(VisitTimeAlreadyTakenException("Visit for given time frame with doctor or patient already exists."))
                false -> Either.Right(visit)
            }
        }

    data class Input(val visitId: Long, val time: LocalTime)
}