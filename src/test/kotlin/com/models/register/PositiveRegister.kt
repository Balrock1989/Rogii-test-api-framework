package com.models.register

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class PositiveRegister(
        @Required val id: Int = 0,
        @Required val token: String = "",
)
