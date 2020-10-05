package com.models.response.users

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class UpdatedUserModel(
        @Required var updatedAt: String,
        var name: String? = null,
        var job: String? = null,
        var invalidIntField: Int? = null,
        var invalidBooleanField: Boolean? = null,
)