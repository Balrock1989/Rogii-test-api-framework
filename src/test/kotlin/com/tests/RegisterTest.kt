package com.tests

import com.BaseTest
import com.data.DataBank
import com.models.request.RegisterBody
import com.models.response.register.NegativeRegister
import com.models.response.register.PositiveRegister
import kotlinx.serialization.json.Json
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class RegisterTest : BaseTest() {

    @DataProvider
    fun positiveBody(): Array<Array<String>> {
        return arrayOf(
                arrayOf(RegisterBody(DataBank.LOGIN_ADMIN.get(), DataBank.PASSWORD_ADMIN.get()).getBody()),
                arrayOf(RegisterBody(DataBank.LOGIN_ADMIN.get(), "1").getBody()),
                arrayOf(RegisterBody(DataBank.LOGIN_ADMIN.get(), "numbers123456789").getBody()),
                arrayOf(RegisterBody(DataBank.LOGIN_ADMIN.get(), getRandomString(200)).getBody()),
        )
    }

    @Test(description = "Позитивная регистрация", dataProvider = "positiveBody")
    fun positiveRegisterTest(body: String) {
        post(DataBank.REGISTER_URL.get(), body).use {
            checkStatus(it.code, 200)
            val response: PositiveRegister = Json.decodeFromString(PositiveRegister.serializer(), it.body!!.string())
            assertThat(response.token.length, equalTo(17))
            assertThat(response.id, notNullValue())
        }
    }

    @DataProvider
    fun invalidBody(): Array<Array<Any>> {
        return arrayOf(
                arrayOf(RegisterBody("OtherLogin@reqres.in", DataBank.PASSWORD_ADMIN.get()).getBody(), "Note: Only defined users succeed registration"),
                arrayOf(RegisterBody(DataBank.LOGIN_ADMIN.get(), "").getBody(), "Missing password"),
                arrayOf(RegisterBody("", getRandomString(15)).getBody(), "Missing email or username"),
                arrayOf("", "Missing email or username"),
        )
    }

    @Test(description = "Проверка ошибок при неверном теле запроса", dataProvider = "invalidBody")
    fun registerWithInvalidDataTest(body: String, message: String) {
        post(DataBank.REGISTER_URL.get(), body).use {
            checkStatus(it.code, 400)
            val response: NegativeRegister = Json.decodeFromString(NegativeRegister.serializer(), it.body!!.string())
            assertThat(response.error, equalTo(message))
        }
    }
}