package com.tests.register

import com.BaseTest
import com.api.Status
import com.data.DataBank
import com.models.request.register.RegisterModel
import com.models.response.register.NegativeRegisterModel
import com.models.response.register.PositiveRegisterModel
import kotlinx.serialization.json.Json
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.json.JSONObject
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class RegisterTest : BaseTest() {

    @DataProvider
    fun positiveBody(): Array<Array<String>> {
        return arrayOf(
                arrayOf(RegisterModel(DataBank.LOGIN_ADMIN.get(), DataBank.PASSWORD_ADMIN.get()).getBody()),
                arrayOf(RegisterModel(DataBank.LOGIN_ADMIN.get(), "1").getBody()),
                arrayOf(RegisterModel(DataBank.LOGIN_ADMIN.get(), "numbers123456789").getBody()),
                arrayOf(RegisterModel(DataBank.LOGIN_ADMIN.get(), getRandomString(200)).getBody()),
        )
    }

    @Test(description = "Позитивная регистрация", dataProvider = "positiveBody")
    fun positiveRegisterTest(body: String) {
        val registerJson: JSONObject = post(DataBank.REGISTER_URL.get(), body, Status.OK.code)
        val response: PositiveRegisterModel = Json.decodeFromString(PositiveRegisterModel.serializer(), registerJson.toString())
        assertThat(response.token.length, equalTo(17))
        assertThat(response.id, notNullValue())
    }

    @DataProvider
    fun invalidBody(): Array<Array<Any>> {
        return arrayOf(
                arrayOf(RegisterModel("OtherLogin@reqres.in", DataBank.PASSWORD_ADMIN.get()).getBody(), "Note: Only defined users succeed registration"),
                arrayOf(RegisterModel(DataBank.LOGIN_ADMIN.get(), "").getBody(), "Missing password"),
                arrayOf(RegisterModel("", getRandomString(15)).getBody(), "Missing email or username"),
                arrayOf("", "Missing email or username"),
        )
    }

    @Test(description = "Проверка ошибок при неверном теле запроса", dataProvider = "invalidBody")
    fun registerWithInvalidDataTest(body: String, message: String) {
        val registerJson: JSONObject = post(DataBank.REGISTER_URL.get(), body, Status.BAD_REQUEST.code)
        val response: NegativeRegisterModel = Json.decodeFromString(NegativeRegisterModel.serializer(), registerJson.toString())
        assertThat(response.error, equalTo(message))
    }
}