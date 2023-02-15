package net.kryszak.healme.doctor

import arrow.core.Either

interface VisitStore {

    fun deleteVisits(doctorId: Long): Either<Throwable, Unit>
}