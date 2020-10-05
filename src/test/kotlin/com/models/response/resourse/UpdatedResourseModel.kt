package com.models.response.resourse

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class UpdatedResourseModel(
        @Required var updatedAt: String,
        var name: String? = null,
        var job: String? = null,
        var invalidIntField: Int? = null,
        var invalidBooleanField: Boolean? = null,
)