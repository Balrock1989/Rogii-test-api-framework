package com.models.response.users

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import org.testng.annotations.Optional

@Serializable
data class Data(
        @Optional val avatar: String,
        @Required val email: String,
        @Required val first_name: String,
        @Required val id: Int,
        @Required val last_name: String
)