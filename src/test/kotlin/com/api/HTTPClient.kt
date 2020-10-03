package com.api

import com.common.LoggingInterceptor
import io.qameta.allure.okhttp3.AllureOkHttp3
import okhttp3.Headers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient

open class HTTPClient {
    val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
    val headers: Headers = Headers.Builder()
            .add("Accept-Encoding", "identity")
            .build()

    val client: OkHttpClient
    init{
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        builder.addInterceptor(AllureOkHttp3())
        if (System.getProperty("config.logger").toBoolean()) builder.addNetworkInterceptor(LoggingInterceptor())
        client = builder.build()
    }
}