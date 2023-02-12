package net.kryszak.healme.authentication

import arrow.core.Either
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import java.util.UUID
import net.kryszak.healme.authentication.GetTenantQuery
import net.kryszak.healme.authentication.GetTenantQuery.Input
import net.kryszak.healme.authentication.TenantId
import net.kryszak.healme.authentication.TenantStore
import net.kryszak.healme.common.exception.DataNotFoundException

class GetTenantQueryTest : ShouldSpec({

    val tenantStore = mockk<TenantStore>()
    val query = GetTenantQuery(tenantStore)

    should("find tenant id for given api key") {
        //given
        val apiKey = "exists"
        val tenantId = TenantId(UUID.randomUUID())
        every { tenantStore.findTenant(apiKey) } returns Either.Right(tenantId)

        //when
        val result = query.execute(Input(apiKey))

        //then
        result shouldBeRight tenantId
    }

    should("return exception if tenant for given api key does not exist") {
        //given
        val apiKey = "does-not-exists"
        val exception = DataNotFoundException()
        every { tenantStore.findTenant(apiKey) } returns Either.Left(exception)

        //when
        val result = query.execute(Input(apiKey))

        //then
        result shouldBeLeft exception
    }
})