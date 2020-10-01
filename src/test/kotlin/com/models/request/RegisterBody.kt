package com.models.request

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
class RegisterBody(
        @Required val email: String,
        @Required val password: String,

        ) {
    fun getBody(): String {
        return Json.encodeToString(serializer(), RegisterBody(email, password))
    }
}
