package com.models.response.register

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class PositiveRegisterModel(
        @Required val id: Int,
        @Required val token: String,
)
