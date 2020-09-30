package com.api.models.users

import kotlinx.serialization.Serializable

@Serializable
data class Ad(
    val company: String,
    val text: String,
    val url: String
)