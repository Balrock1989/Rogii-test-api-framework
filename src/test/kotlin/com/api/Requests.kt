package com.api

import io.qameta.allure.Step
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

open class Requests : ApiRequestHelper(){

    @Step("POST запрос")
    fun post(url: String, body: RequestBody): Response {
        val request = Request.Builder()
                .url(url)
                .headers(headers)
                .post(body)
                .build()
        return client.newCall(request).execute()
    }

    @Step("GET запрос")
    fun get(url: String): Response {
        val request = Request.Builder()
                .url(url)
                .headers(headers)
                .get()
                .build()
        return client.newCall(request).execute()
    }
}