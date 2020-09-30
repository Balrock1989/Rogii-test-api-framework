package com.api.helpers

import LoggingInterceptor
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType


object ApiRequestHelper {

    private val client: OkHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(LoggingInterceptor())
            .build()

    private val JSON: MediaType = "application/json; charset=utf-8".toMediaType()

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

    fun post(url: String, body: RequestBody): Response {
        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()
        return client.newCall(request).execute()
    }
}