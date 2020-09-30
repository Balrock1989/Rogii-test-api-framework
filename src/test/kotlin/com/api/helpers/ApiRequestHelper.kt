package com.api.helpers

import LoggingInterceptor
import io.qameta.allure.Step
import io.qameta.allure.okhttp3.AllureOkHttp3
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType


open class ApiRequestHelper {
    private val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
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

//    fun getBearerToken(url: String, body: RequestBody, headers: Headers): String {
//        val request = Request.Builder()
//                .url(url)
//                .post(body)
//                .headers(headers)
//                .build()
//        val response = client.newCall(request).execute()
//        Assert.assertEquals(response.code, 200)
//        val result = response.body.toString()
//        return JSONObject(result).get("token").toString()
//    }
    @Step("POST запрос")
    fun post(url: String, body: RequestBody): Response {
        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()
        return client.newCall(request).execute()
    }
}