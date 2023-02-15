package net.kryszak.healme.patient

import arrow.core.Either

interface VisitStore {

    fun deleteVisits(patientId: Long): Either<Throwable, Unit>
}