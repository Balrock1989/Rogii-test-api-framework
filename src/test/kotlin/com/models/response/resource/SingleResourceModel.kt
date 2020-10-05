package com.models.response.resource

import com.models.general.AdModel
import com.models.general.dataObjects.ResourceDataModel
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class SingleResourceModel(
        @Required var ad: AdModel,
        @Required var data: ResourceDataModel
)