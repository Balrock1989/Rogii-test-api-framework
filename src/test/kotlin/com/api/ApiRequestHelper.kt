package com.api

import com.common.LoggingInterceptor
import com.models.response.users.DataModel
import io.qameta.allure.Step
import io.qameta.allure.okhttp3.AllureOkHttp3
import okhttp3.Headers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import java.util.stream.Collectors


open class ApiRequestHelper {
    val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    val headers: Headers = Headers.Builder()
            .add("Accept-Encoding", "identity")
            .build()
    lateinit var client: OkHttpClient
    fun initOkHTTPClientWithLogger() {
        client = OkHttpClient.Builder()
                .addNetworkInterceptor(LoggingInterceptor())
                .addInterceptor(AllureOkHttp3())
                .build()
    }

    fun initOkHTTPClient() {
        client = OkHttpClient.Builder()
                .addInterceptor(AllureOkHttp3())
                .build()
    }

    @Step("Проверка кода ответа сервера")
    fun checkStatus(code: Int, expectedCode: Int) {
        assertThat(code, equalTo(expectedCode))
    }

    @Step("Проверка сортировки пользователей по Id")
    fun checkSortedUsersById(users: List<DataModel>) {
        val usersId: List<Int> = users.stream().map { user -> user.id }.collect(Collectors.toList())
        assertThat(usersId, equalTo(usersId.sorted()))
    }
}