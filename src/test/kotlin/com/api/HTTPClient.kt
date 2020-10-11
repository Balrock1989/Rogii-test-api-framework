package com.api

import com.common.LoggingInterceptor
import io.qameta.allure.okhttp3.AllureOkHttp3
import okhttp3.Headers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import java.net.InetSocketAddress
import java.net.Proxy
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


/*** Конфигурация HTTP клиента*/
open class HTTPClient {
    val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
    val headers: Headers = Headers.Builder()
            .add("Accept-Encoding", "identity")
            .build()
    var proxy: Proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("127.0.0.1", 8888))
    val client =  if (System.getProperty("config.proxy").toBoolean()) getUnsafeOkHttpClient() else getOkHTTPClient()

    private fun getUnsafeOkHttpClient(): OkHttpClient {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(
                    object : X509TrustManager {
                        @Throws(CertificateException::class)
                        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}

                        @Throws(CertificateException::class)
                        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}

                        override fun getAcceptedIssuers(): Array<X509Certificate?> { return arrayOfNulls(0) }
                    }
            )
            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, null)
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            val builder: OkHttpClient.Builder = OkHttpClient.Builder()
            builder.addInterceptor(AllureOkHttp3())
            if (System.getProperty("config.logger").toBoolean()) builder.addNetworkInterceptor(LoggingInterceptor())
            builder.proxy(proxy)
            builder.sslSocketFactory(sslSocketFactory, (trustAllCerts[0] as X509TrustManager)).build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
    private fun getOkHTTPClient(): OkHttpClient{
        return try {
            val builder: OkHttpClient.Builder = OkHttpClient.Builder()
            builder.addInterceptor(AllureOkHttp3())
            if (System.getProperty("config.logger").toBoolean()) builder.addNetworkInterceptor(LoggingInterceptor())
            builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}