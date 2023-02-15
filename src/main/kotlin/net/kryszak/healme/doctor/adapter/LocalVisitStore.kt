package net.kryszak.healme.doctor.adapter

import arrow.core.Either
import net.kryszak.healme.doctor.VisitStore
import net.kryszak.healme.visit.port.VisitFacade

class LocalVisitStore(private val visitFacade: VisitFacade) : VisitStore {
    override fun deleteVisits(doctorId: Long): Either<Throwable, Unit> =
        visitFacade.deleteVisits(doctorId, VisitFacade.DeleteBy.DOCTOR)
}