package com.models.request.register

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class RegisterModel(
        @Required val email: String,
        @Required val password: String,

        ) {
    fun getBody(): String {
        return Json.encodeToString(serializer(), this)
    }
}
