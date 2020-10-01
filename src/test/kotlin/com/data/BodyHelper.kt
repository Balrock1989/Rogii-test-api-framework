package com.data

import okhttp3.FormBody
import okhttp3.RequestBody

open class BodyHelper {

    fun getAuthBody(login: String, password: String): RequestBody {
        return FormBody.Builder()
                .add("email", login)
                .add("password", password)
                .build()
    }
}