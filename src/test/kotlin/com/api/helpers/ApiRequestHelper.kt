package api.helpers

import com.sun.net.httpserver.Headers
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONObject
import org.testng.Assert
import java.awt.PageAttributes
import java.io.IOException


object ApiRequestHelper {
    private val client: OkHttpClient = OkHttpClient()
//    private val JSON: PageAttributes.MediaType = "application/json; charset=utf-8".toMediaType()

    fun executeRequest(request:Request) : Response {
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
                .build();
        return client.newCall(request).execute()
    }

}