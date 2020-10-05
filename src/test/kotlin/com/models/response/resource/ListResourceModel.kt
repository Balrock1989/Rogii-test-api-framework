package com.models.response.resource

import com.models.general.AdModel
import com.models.general.dataObjects.ResourceDataModel
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class ListResourceModel(
        @Required var ad: AdModel,
        @Required var data: List<ResourceDataModel>,
        @Required var page: Int,
        @Required var per_page: Int,
        @Required var total: Int,
        @Required var total_pages: Int
)