package net.kryszak.healme.common

import arrow.core.Either
import net.kryszak.healme.authentication.TenantId

interface TenantStore {

    fun getCurrentTenant(): Either<Throwable, TenantId>
}