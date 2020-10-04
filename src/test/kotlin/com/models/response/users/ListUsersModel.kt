package com.models.response.users

import com.models.general.dataObjects.UserDataModel
import com.models.general.AdModel
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class ListUsersModel(
        @Required val ad: AdModel,
        @Required val data: List<UserDataModel>,
        @Required val page: Int,
        @Required val per_page: Int,
        @Required val total: Int,
        @Required val total_pages: Int
)