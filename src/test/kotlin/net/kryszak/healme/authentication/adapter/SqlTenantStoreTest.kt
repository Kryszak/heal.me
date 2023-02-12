package net.kryszak.healme.authentication.adapter

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import java.util.UUID
import net.kryszak.healme.authentication.TenantId
import net.kryszak.healme.common.exception.DataNotFoundException

class SqlTenantStoreTest : ShouldSpec({

    val tenantRepository = mockk<TenantRepository>()
    val store = SqlTenantStore(tenantRepository)

    should("find tenant id for existing api key") {
        //given
        val apiKey = "exists"
        val tenantUuid = UUID.randomUUID()
        val tenantEntity = TenantEntity().apply {
            this.id = tenantUuid
            this.apiKey = apiKey
        }
        every { tenantRepository.findByApiKey(apiKey) } returns tenantEntity

        //when
        val result = store.findTenant(apiKey)

        //then
        result shouldBeRight TenantId(tenantUuid)
    }

    should("return left with exception for non existing api key") {
        //given
        val apiKey = "not existing"
        every { tenantRepository.findByApiKey(apiKey) } returns null

        //when
        val result = store.findTenant(apiKey)

        //then
        result shouldBeLeft DataNotFoundException("Tenant with Api Key = {not existing} not found.")
    }

    should("return exception if communication with db fails") {
        //given
        val exception = Exception()
        every { tenantRepository.findByApiKey(any()) } throws exception

        //when
        val result = store.findTenant("key")

        //then
        result shouldBeLeft exception
    }
})