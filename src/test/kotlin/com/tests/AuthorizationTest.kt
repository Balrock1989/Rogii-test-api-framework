package tests

import api.data.DataBank
import api.data.getAuthBody
import api.helpers.ApiRequestHelper.post
import api.models.AuthModel
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.testng.annotations.Test

class AuthorizationTest {

    @Test(description = "Positive Authorization Test")
    fun PositiveAuthorizationTest() {
        val body: RequestBody = getAuthBody(DataBank.LOGIN_ADMIN.get(), DataBank.PASSWORD_ADMIN.get())
        post(DataBank.LOGIN_URL.get(), body).use{ response ->
            assertThat(response.code, equalTo(200))
            val authResponse = Json.decodeFromString(AuthModel.serializer(), response.body!!.string())
            assertThat(authResponse.token.length, equalTo(17))
            assertThat(authResponse.id, notNullValue())
        }
    }
}