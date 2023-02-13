package net.kryszak.healme.patient

import net.kryszak.healme.authentication.TenantId

data class Patient(
    val id: Long,
    val name: String,
    val surname: String,
    val address: String,
    val owner: TenantId,
)
