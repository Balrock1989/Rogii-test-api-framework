package com.api

import io.qameta.allure.Step
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject

open class Requests : ApiRequestHelper() {

    @Step("POST запрос")
    fun post(url: String, body: String, expectedCode: Int): JSONObject {
        val requestBody: RequestBody = body.toRequestBody(JSON)
        val request = Request.Builder()
                .url(url)
                .headers(headers)
                .post(requestBody)
                .build()
        val response: Response = client.newCall(request).execute()
        checkStatus(response.code, expectedCode)
        return JSONObject(response.body!!.string())
    }

    @Step("GET запрос")
    fun get(url: String, expectedCode: Int): JSONObject {
        val request = Request.Builder()
                .url(url)
                .headers(headers)
                .get()
                .build()
        val response: Response = client.newCall(request).execute()
        checkStatus(response.code, expectedCode)
        return JSONObject(response.body!!.string())
    }
}