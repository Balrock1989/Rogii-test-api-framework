package com.models.response.users

import com.models.response.general.AdModel
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class SingleUserModel(
        @Required val ad: AdModel,
        @Required val data: UserDataModel
)