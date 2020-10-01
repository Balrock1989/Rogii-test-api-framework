package com.models.response.users

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class Users(
        @Required val ad: Ad,
        @Required val data: List<Data>,
        @Required val page: Int,
        @Required val per_page: Int,
        @Required val total: Int,
        @Required val total_pages: Int
)