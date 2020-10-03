package com.models.request.resource

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
class CreateResourceModel(
        val job: String,
        val name: String
) {
    fun getBody(): String {
        return Json.encodeToString(serializer(), this)
    }
}