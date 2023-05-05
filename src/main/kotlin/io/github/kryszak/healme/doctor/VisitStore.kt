package io.github.kryszak.healme.doctor

import arrow.core.Either

interface VisitStore {

    fun deleteVisits(doctorId: Long): Either<Throwable, Unit>
}