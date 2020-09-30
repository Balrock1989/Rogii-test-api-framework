package com.api.helpers

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.net.InetSocketAddress
import java.net.Proxy


object ApiRequestHelper {

     val client: OkHttpClient = OkHttpClient()


//    private val JSON: PageAttributes.MediaType = "application/json; charset=utf-8".toMediaType()

    fun executeRequest(request: Request) : Response {
        return client.newCall(request).execute()
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

    fun post(url: String, body: RequestBody) : Response {
        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()
        return client.newCall(request).execute()
    }

}