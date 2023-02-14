package net.kryszak.healme.doctor

import net.kryszak.healme.authentication.TenantId

data class Doctor(
    val id: Long,
    val name: String,
    val surname: String,
    // NOTE: this should be a proper enum class. For simplicity reason,
    // it's handled as single string field
    val specialization: String,
    val owner: TenantId,
)
