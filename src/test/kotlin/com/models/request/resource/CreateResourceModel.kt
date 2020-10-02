package com.models.request.resource

import com.models.request.register.RegisterModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
class CreateResourceModel(
        val job: String,
        val name: String
) {
    fun getBody(): String {
        return Json.encodeToString(RegisterModel.serializer(), RegisterModel(job, name))
    }
}