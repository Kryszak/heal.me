package net.kryszak.healme.patient

import net.kryszak.healme.authentication.TenantId

data class Patient(
    val id: Long,
    val name: String,
    val surname: String,
    // NOTE: this should be a proper value object. For simplicity reason,
    // it's handled as single string field
    val address: String,
    val owner: TenantId,
)
