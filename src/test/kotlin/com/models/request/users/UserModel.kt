package com.models.request.users

data class UserModel(
        val id: Int,
        val email: String,
        val firstName: String,
        val lastName: String,
        val avatar: String
) {
}