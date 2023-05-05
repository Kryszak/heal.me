package io.github.kryszak.healme.doctor.adapter

import arrow.core.Either
import io.github.kryszak.healme.doctor.VisitStore
import io.github.kryszak.healme.visit.port.VisitFacade

class LocalVisitStore(private val visitFacade: VisitFacade) : VisitStore {
    override fun deleteVisits(doctorId: Long): Either<Throwable, Unit> =
        visitFacade.deleteVisits(doctorId, VisitFacade.DeleteBy.DOCTOR)
}