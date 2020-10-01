package com.models.response.users

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class SingleUserModel(
        @Required val ad: AdModel,
        @Required val data: DataModel
)