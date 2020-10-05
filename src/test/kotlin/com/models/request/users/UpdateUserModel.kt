package com.models.request.users

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class UpdateUserModel(
        val job: String,
        val name: String
) {
    fun getBody(): String {
        return Json.encodeToString(serializer(), this)
    }
}