package io.github.kryszak.healme.common

import arrow.core.Either
import io.github.kryszak.healme.authentication.TenantId

interface TenantStore {

    fun getCurrentTenant(): Either<Throwable, TenantId>
}