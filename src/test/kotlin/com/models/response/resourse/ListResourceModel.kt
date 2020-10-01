package com.models.response.resourse

import com.models.response.general.AdModel
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class ListResourceModel(
        @Required val ad: AdModel,
        @Required val data: List<ResourceDataModel>,
        @Required val page: Int,
        @Required val per_page: Int,
        @Required val total: Int,
        @Required val total_pages: Int
)