package com.tests

import com.data.DataBank
import com.BaseTest
import com.models.register.NegativeRegister
import com.models.register.PositiveRegister
import kotlinx.serialization.json.Json
import okhttp3.FormBody
import okhttp3.RequestBody
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class RegisterTest : BaseTest() {

    @DataProvider
    fun positiveBody(): Array<Array<RequestBody>> {
        return arrayOf(
                arrayOf(getAuthBody(DataBank.LOGIN_ADMIN.get(), DataBank.PASSWORD_ADMIN.get())),
                arrayOf(getAuthBody(DataBank.LOGIN_ADMIN.get(), "1")),
                arrayOf(getAuthBody(DataBank.LOGIN_ADMIN.get(), "numbers123456789")),
                arrayOf(getAuthBody(DataBank.LOGIN_ADMIN.get(), getRandomString(200)))
        )
    }

    @Test(description = "Позитивная регистрация", dataProvider = "positiveBody")
    fun positiveRegisterTest(body: RequestBody) {
        post(DataBank.REGISTER_URL.get(), body).use {
            assertThat(it.code, equalTo(200))
            val response: PositiveRegister = Json.decodeFromString(PositiveRegister.serializer(), it.body!!.string())
            assertThat(response.token.length, equalTo(17))
            assertThat(response.id, notNullValue())
        }
    }

    @DataProvider
    fun invalidBody(): Array<Array<Any>> {
        return arrayOf(
                arrayOf(getAuthBody("OtherLogin@reqres.in", DataBank.PASSWORD_ADMIN.get()), "Note: Only defined users succeed registration"),
                arrayOf(getAuthBody(DataBank.LOGIN_ADMIN.get(), ""), "Missing password"),
                arrayOf(getAuthBody("", getRandomString(15)), "Missing email or username"),
                arrayOf(FormBody.Builder().build(), "Missing email or username")
        )
    }

    @Test(description = "Проверка ошибок при неверном теле запроса", dataProvider = "invalidBody")
    fun registerWithInvalidDataTest(body: RequestBody, message: String) {
        post(DataBank.REGISTER_URL.get(), body).use {
            assertThat(it.code, equalTo(400))
            val response: NegativeRegister = Json.decodeFromString(NegativeRegister.serializer(), it.body!!.string())
            assertThat(response.error, equalTo(message))
        }
    }
}