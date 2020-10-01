package com.models.response.users

import kotlinx.serialization.Serializable
import org.testng.annotations.Optional

@Serializable
data class Ad(
        @Optional val company: String,
        @Optional val text: String,
        @Optional val url: String
)