package com.api

import com.common.LoggingInterceptor
import com.data.BodyHelper
import io.qameta.allure.okhttp3.AllureOkHttp3
import okhttp3.Headers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient


open class ApiRequestHelper : BodyHelper() {
    val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    val headers: Headers = Headers.Builder()
            .add("Content-Type", "application/json;charset=UTF-8")
            .add("Accept-Encoding", "identity")
            .build()
    lateinit var client: OkHttpClient
    fun initOkHTTPClientWithLogger() {
        client = OkHttpClient.Builder()
                .addNetworkInterceptor(LoggingInterceptor())
                .addInterceptor(AllureOkHttp3())
                .build()
    }

    fun initOkHTTPClient() {
        client = OkHttpClient.Builder()
                .addInterceptor(AllureOkHttp3())
                .build()
    }
}