package net.kryszak.healme.visit.port

import arrow.core.Either
import net.kryszak.healme.visit.DeleteVisitsCommand

class VisitFacade(private val deleteVisitsCommand: DeleteVisitsCommand) {

    fun deleteVisits(id: Long, deleteBy: DeleteBy): Either<Throwable, Unit> =
        deleteVisitsCommand.execute(DeleteVisitsCommand.Input(id, deleteBy.toCommandInputValue()))

    enum class DeleteBy {
        PATIENT, DOCTOR;

        fun toCommandInputValue(): DeleteVisitsCommand.DeleteBy {
            return when (this) {
                PATIENT -> DeleteVisitsCommand.DeleteBy.PATIENT
                DOCTOR -> DeleteVisitsCommand.DeleteBy.DOCTOR
            }
        }
    }
}