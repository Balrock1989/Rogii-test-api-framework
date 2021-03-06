package com.models.response.users

import com.models.general.dataObjects.UserDataModel
import com.models.general.AdModel
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class ListUsersModel(
        @Required var ad: AdModel,
        @Required var data: List<UserDataModel>,
        @Required var page: Int,
        @Required var per_page: Int,
        @Required var total: Int,
        @Required var total_pages: Int
)