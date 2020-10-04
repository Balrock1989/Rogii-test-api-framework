package com.models.response.users

import com.models.general.dataObjects.UserDataModel
import com.models.general.AdModel
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class SingleUserModel(
        @Required val ad: AdModel,
        @Required val data: UserDataModel
)