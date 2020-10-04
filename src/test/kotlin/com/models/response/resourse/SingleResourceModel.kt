package com.models.response.resourse

import com.models.general.AdModel
import com.models.general.dataObjects.ResourceDataModel
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class SingleResourceModel(
        @Required val ad: AdModel,
        @Required val data: ResourceDataModel
)