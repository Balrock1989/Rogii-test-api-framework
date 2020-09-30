package com.api.helpers

import LoggingInterceptor
import io.qameta.allure.Step
import io.qameta.allure.okhttp3.AllureOkHttp3
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType


open class ApiRequestHelper {
    private val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    private val headers: Headers = Headers.Builder()
                .add("Content-Type", "application/json;charset=UTF-8")
                .add("Accept-Encoding", "identity")
                .build()
    lateinit var client: OkHttpClient
    fun initOkHTTPClientWithLogger()  {
        client= OkHttpClient.Builder()
                .addNetworkInterceptor(LoggingInterceptor())
                .addInterceptor(AllureOkHttp3())
                .build()
    }

    fun initOkHTTPClient() {
        client= OkHttpClient.Builder()
                .addInterceptor(AllureOkHttp3())
                .build()
    }

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