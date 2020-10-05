package com.models.response.resource

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class UpdatedResourceModel(
        @Required var updatedAt: String,
        var name: String? = null,
        var job: String? = null,
        var invalidIntField: Int? = null,
        var invalidBooleanField: Boolean? = null,
)