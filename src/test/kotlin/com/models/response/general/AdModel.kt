package com.models.response.general

import kotlinx.serialization.Serializable
import org.testng.annotations.Optional

@Serializable
data class AdModel(
        @Optional val company: String,
        @Optional val text: String,
        @Optional val url: String
)