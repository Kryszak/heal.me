package io.github.kryszak.healme.patient

data class Patient(
    val id: Long,
    val name: String,
    val surname: String,
    // NOTE: this should be a proper value object. For simplicity reason,
    // it's handled as single string field
    val address: String,
    val owner: io.github.kryszak.healme.authentication.TenantId,
)
