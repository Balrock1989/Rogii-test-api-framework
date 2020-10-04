package com.tests.users

import com.BaseTest
import com.api.Endpoints
import com.api.Status
import com.data.DataBank
import com.models.request.users.UpdateUserModel
import com.models.response.users.SingleUserModel
import kotlinx.serialization.json.Json
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.text.MatchesPattern.matchesPattern
import org.json.JSONObject
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class SingleUserTest : BaseTest() {

    @DataProvider
    fun positiveUsersId(): Array<Array<Int>> {
        return arrayOf(
                arrayOf(1),
                arrayOf(12)
        )
    }

    @Test(description = "Валидация деталей ответа для одного пользователя", dataProvider = "positiveUsersId")
    fun validateSingleUsersDetailsTest(userId: Int) {
        val usersJson: JSONObject = get(Endpoints.USERS.URL + "/$userId", Status.OK.code)
        val user = Json.decodeFromString(SingleUserModel.serializer(), usersJson.toString())
        assertThat(user.ad.company, equalTo(DataBank.AD_COMPANY.get()))
        assertThat(user.ad.text, equalTo(DataBank.AD_TEXT.get()))
        assertThat(user.ad.url, equalTo(DataBank.AD_URL.get()))
        assertThat(user.data.id, equalTo(userId))
        assertThat(user.data.firstName, matchesPattern(DataBank.USER_NAME_PATTERN.get()))
        assertThat(user.data.lastName, matchesPattern(DataBank.USER_NAME_PATTERN.get()))
        assertThat(user.data.email, matchesPattern(DataBank.EMAIL_PATTERN.get()))
        assertThat(user.data.avatar, matchesPattern(DataBank.URL_PATTERN.get()))
    }

    @Test(description = "Обновление пользователя", dataProvider = "positiveUsersId")
    fun positiveUpdateUserTest(userId: Int) {
        patch(Endpoints.USERS.URL + "/$userId", UpdateUserModel("morpheus", "zion resident").getBody(), Status.OK.code)
    }

    @DataProvider
    fun invalidUsersId(): Array<Array<String>> {
        return arrayOf(
                arrayOf("/0"),
                arrayOf("/13"),
                arrayOf("/-1"),
                arrayOf("/User"),
        )
    }

    @Test(description = "Проверка результатов запроса с неверными параметрами", dataProvider = "invalidUsersId")
    fun invalidBodyForSingleUserTest(userId: String) {
        val usersJson: JSONObject = get(Endpoints.USERS.URL + userId, Status.NOT_FOUND.code)
        assertThat("{}", equalTo(usersJson.toString()))
    }

    @Test(description = "Удаление пользователя", dataProvider = "invalidUsersId")
    fun positiveDeleteUserTest(userId: String) {
        delete(Endpoints.USERS.URL + userId, Status.NO_CONTENT.code)
    }
}