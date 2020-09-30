import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.concurrent.TaskRunner.Companion.logger
import okio.Buffer
import java.io.IOException

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
        if (request.method.equals("GET")) {
            logger.info(java.lang.String.format("GET " + F_REQUEST_WITHOUT_BODY + F_RESPONSE_WITH_BODY, request.url, time, request.headers, response.code, stringifyResponseBody(bodyString)))
        } else if (request.method.equals("POST")) {
            logger.info(java.lang.String.format("POST " + F_REQUEST_WITH_BODY + F_RESPONSE_WITH_BODY, request.url, time, request.headers, stringifyRequestBody(request), response.code, stringifyResponseBody(bodyString)))
        } else if (request.method.equals("PUT")) {
            logger.info(java.lang.String.format("PUT " + F_REQUEST_WITH_BODY + F_RESPONSE_WITH_BODY, request.url, time, request.headers, request.body.toString(), response.code, stringifyResponseBody(bodyString)))
        } else if (request.method.equals("DELETE")) {
            logger.info(java.lang.String.format("DELETE " + F_REQUEST_WITHOUT_BODY + F_RESPONSE_WITHOUT_BODY, request.url, time, request.headers, response.code))
        }
        return if (response.body != null) {
            val body: ResponseBody = bodyString!!.toResponseBody(contentType)
            response.newBuilder().body(body).build()
        } else {
            response
        }
    }

    fun stringifyResponseBody(responseBody: String?): String? {
        return responseBody
    }

    companion object {
        private const val F_BREAK = " %n"
        private const val F_URL = " %s"
        private const val F_TIME = " in %.1fms"
        private const val F_HEADERS = "%s"
        private const val F_RESPONSE = F_BREAK + "Response: %d"
        private const val F_BODY = "body: %s"
        private const val F_BREAKER = F_BREAK + "-------------------------------------------" + F_BREAK
        private const val F_REQUEST_WITHOUT_BODY = F_URL + F_TIME + F_BREAK + F_HEADERS
        private const val F_RESPONSE_WITHOUT_BODY = F_RESPONSE + F_BREAK + F_BREAKER
        private const val F_REQUEST_WITH_BODY = F_URL + F_TIME + F_BREAK + F_HEADERS + F_BODY + F_BREAK
        private const val F_RESPONSE_WITH_BODY = F_RESPONSE + F_BREAK + F_BODY + F_BREAK + F_BREAKER
        private fun stringifyRequestBody(request: Request): String {
            return try {
                val copy: Request = request.newBuilder().build()
                val buffer = Buffer()
                copy.body!!.writeTo(buffer)
                buffer.readUtf8()
            } catch (e: IOException) {
                "did not work"
            }
        }
    }
}