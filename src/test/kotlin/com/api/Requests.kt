package com.api

import io.qameta.allure.Step
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

/*** Реализация запросов*/
open class Requests : ApiRequestHelper() {

    @Step("POST запрос")
    fun post(url: String, body: String, expectedCode: Int): JSONObject {
        val requestBody: RequestBody = body.toRequestBody(mediaType)
        val request = Request.Builder()
                .url(url)
                .headers(headers)
                .post(requestBody)
                .build()
        client.newCall(request).execute().use {
            checkStatus(it.code, expectedCode)
            return JSONObject(it.body!!.string())
        }
    }

    @Step("GET запрос")
    fun get(url: String, expectedCode: Int): JSONObject {
        val request = Request.Builder()
                .url(url)
                .headers(headers)
                .get()
                .build()
        client.newCall(request).execute().use {
            checkStatus(it.code, expectedCode)
            return JSONObject(it.body!!.string())
        }
    }
}