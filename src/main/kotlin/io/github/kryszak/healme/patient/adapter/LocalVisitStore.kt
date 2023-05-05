package io.github.kryszak.healme.patient.adapter

import arrow.core.Either
import io.github.kryszak.healme.patient.VisitStore
import io.github.kryszak.healme.visit.port.VisitFacade

class LocalVisitStore(private val visitFacade: VisitFacade) : VisitStore {
    override fun deleteVisits(patientId: Long): Either<Throwable, Unit> {
        return visitFacade.deleteVisits(patientId, VisitFacade.DeleteBy.PATIENT)
    }
}