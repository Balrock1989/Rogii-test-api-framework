package com.models.response.register

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class NegativeRegisterModel(
        @Required val error: String
)
