package io.github.kryszak.healme.authentication

import java.util.*

const val API_KEY_HEADER = "x-api-key"
const val VALID_API_KEY = "test-integration"
const val INVALID_API_KEY = "i-do-not-exist"

val TENANT_ID = TenantId(UUID.fromString("1bfcfd37-eafa-414a-94be-b377c7399a39"))