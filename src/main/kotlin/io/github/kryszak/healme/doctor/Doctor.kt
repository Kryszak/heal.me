package io.github.kryszak.healme.doctor

data class Doctor(
    val id: Long,
    val name: String,
    val surname: String,
    // NOTE: this should be a proper enum class. For simplicity reason,
    // it's handled as single string field
    val specialization: String,
    val owner: io.github.kryszak.healme.authentication.TenantId,
)
