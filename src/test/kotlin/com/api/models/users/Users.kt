package com.api.models.users

import kotlinx.serialization.Serializable

@Serializable
data class Users(
    val ad: Ad,
    val data: List<Data>,
    val page: Int,
    val per_page: Int,
    val total: Int,
    val total_pages: Int
)