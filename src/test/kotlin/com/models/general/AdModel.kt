package com.models.general

import kotlinx.serialization.Serializable
import org.testng.annotations.Optional

@Serializable
data class AdModel(
        @Optional var company: String,
        @Optional var text: String,
        @Optional var url: String
)