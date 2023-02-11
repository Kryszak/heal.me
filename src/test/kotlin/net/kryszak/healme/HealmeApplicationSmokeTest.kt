package net.kryszak.healme

import io.kotest.core.spec.style.ShouldSpec
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest
class HealmeApplicationSmokeTest : ShouldSpec({
    should("load application context") {}
})
