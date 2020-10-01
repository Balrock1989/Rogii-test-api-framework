package com.models.response.resourse

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class ResourceDataModel(
        @Required val color: String,
        @Required val id: Int,
        @Required val name: String,
        @Required val pantone_value: String,
        @Required val year: Int
)