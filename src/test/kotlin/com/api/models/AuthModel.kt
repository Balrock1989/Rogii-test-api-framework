package api.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthModel(
        val id: Int = 0,
        val token: String = "",
)
