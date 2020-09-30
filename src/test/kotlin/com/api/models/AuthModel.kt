package api.models

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class AuthModel(
        @Required val id: Int = 0,
        @Required val token: String = "",
)
