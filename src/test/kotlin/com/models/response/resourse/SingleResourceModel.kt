package com.models.response.resourse

import com.models.response.general.AdModel
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class SingleResourceModel(
        @Required val ad: AdModel,
        @Required val data: ResourceDataModel
)