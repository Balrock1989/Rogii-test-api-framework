package com.common

import com.cedarsoftware.util.io.JsonIoException
import com.cedarsoftware.util.io.JsonWriter
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.concurrent.TaskRunner.Companion.logger
import okio.Buffer
import java.io.IOException

/*** Перехватчик событий для логирования запросов и ответов*/
internal class LoggingInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val t1 = System.nanoTime()
        val response = chain.proceed(request)
        val t2 = System.nanoTime()
        var contentType: MediaType? = null
        var bodyString: String? = null
        if (response.body != null) {
            contentType = response.body!!.contentType()
            bodyString = response.body!!.string()
        }
        val time = (t2 - t1) / 1e6
        when (request.method) {
            "GET" -> logger.info(java.lang.String.format("GET $F_REQUEST_WITHOUT_BODY$F_RESPONSE_WITH_BODY", request.url, time, request.headers, response.code, stringifyResponseBody(bodyString)))
            "POST" -> logger.info(java.lang.String.format("POST $F_REQUEST_WITH_BODY$F_RESPONSE_WITH_BODY", request.url, time, request.headers, stringifyRequestBody(request), response.code, stringifyResponseBody(bodyString)))
            "PUT" -> logger.info(java.lang.String.format("PUT $F_REQUEST_WITH_BODY$F_RESPONSE_WITH_BODY", request.url, time, request.headers, stringifyRequestBody(request), response.code, stringifyResponseBody(bodyString)))
            "PATCH" -> logger.info(java.lang.String.format("PATCH $F_REQUEST_WITH_BODY$F_RESPONSE_WITH_BODY", request.url, time, request.headers, stringifyRequestBody(request), response.code, stringifyResponseBody(bodyString)))
            "DELETE" -> logger.info(java.lang.String.format("DELETE $F_REQUEST_WITH_BODY$F_RESPONSE_WITH_BODY", request.url, time, request.headers, stringifyRequestBody(request), response.code, stringifyResponseBody(bodyString)))
            else -> {
                logger.severe("Unknown request: " + request.method)
            }
        }

        return if (response.body != null) {
            val body: ResponseBody = bodyString!!.toResponseBody(contentType)
            response.newBuilder().body(body).build()
        } else {
            response
        }
    }

    private fun stringifyResponseBody(responseBody: String?): String? {
        return try{
            JsonWriter.formatJson(responseBody)
        } catch(e: JsonIoException){
            logger.severe("The response body contains invalid JSON")
            responseBody
        }
    }

    companion object {
        private const val F_BREAK = " %n"
        private const val F_URL = " %s"
        private const val F_TIME = " in %.1fms"
        private const val F_HEADERS = "%s"
        private const val F_RESPONSE = F_BREAK + "Response: %d"
        private const val F_BODY = "Request body: %s"
        private const val F_BREAKER = "$F_BREAK-------------------------------------------$F_BREAK"
        private const val F_REQUEST_WITHOUT_BODY = F_URL + F_TIME + F_BREAK + F_HEADERS
        private const val F_RESPONSE_WITHOUT_BODY = F_RESPONSE + F_BREAK + F_BREAKER
        private const val F_REQUEST_WITH_BODY = F_URL + F_TIME + F_BREAK + F_HEADERS + F_BODY + F_BREAK
        private const val F_RESPONSE_WITH_BODY = F_RESPONSE + F_BREAK + F_BODY + F_BREAK + F_BREAKER
        private fun stringifyRequestBody(request: Request): String {
            val copy: Request = request.newBuilder().build()
            val buffer = Buffer()
            copy.body!!.writeTo(buffer)
            return try{
                copy.body!!.writeTo(buffer)
                JsonWriter.formatJson(buffer.readUtf8())
            } catch(e: JsonIoException){
                logger.severe("The request body contains invalid JSON")
                copy.body!!.writeTo(buffer)
                buffer.readUtf8()
            }
        }
    }
}