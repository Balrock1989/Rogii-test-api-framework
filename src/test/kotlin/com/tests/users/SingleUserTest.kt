package com.tests.users

import com.BaseTest
import com.api.Endpoints
import com.api.Status
import com.data.DataBank
import com.models.general.dataObjects.UserDataModel
import com.models.request.users.UpdateUserModel
import com.models.response.users.SingleUserModel
import com.models.response.users.UpdatedUserModel
import kotlinx.serialization.json.Json
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.text.MatchesPattern.matchesPattern
import org.json.JSONObject
import org.testng.annotations.BeforeClass
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class SingleUserTest : BaseTest() {
    private lateinit var exampleUser: UserDataModel

    @BeforeClass
    fun prepare(){
        exampleUser = newUser()
    }
    @DataProvider
    fun positiveUsersId(): Array<Array<Int>> {
        return arrayOf(
                arrayOf(1),
                arrayOf(12)
        )
    }

    @Test(description = "Валидация деталей ответа для одного пользователя", dataProvider = "positiveUsersId")
    fun validateSingleUsersDetailsTest(userId: Int) {
        val response: JSONObject = get(Endpoints.USERS.URL + "/$userId", Status.OK.code)
        val user = Json.decodeFromString(SingleUserModel.serializer(), response.toString())
        assertThat(user.ad.company, equalTo(DataBank.AD_COMPANY.get()))
        assertThat(user.ad.text, equalTo(DataBank.AD_TEXT.get()))
        assertThat(user.ad.url, equalTo(DataBank.AD_URL.get()))
        assertThat(user.data.id, equalTo(userId))
        assertThat(user.data.firstName, matchesPattern(DataBank.USER_NAME_PATTERN.get()))
        assertThat(user.data.lastName, matchesPattern(DataBank.USER_NAME_PATTERN.get()))
        assertThat(user.data.email, matchesPattern(DataBank.EMAIL_PATTERN.get()))
        assertThat(user.data.avatar, matchesPattern(DataBank.URL_PATTERN.get()))
        // Изначально проверяется статус ответа, затем соответствие Json схеме, затем т.к. нам заранее не известны точные имена
        // проверяется соответствие свойств по шаблону регулярного выражения
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
        val response: JSONObject = get(Endpoints.USERS.URL + userId, Status.NOT_FOUND.code)
        assertThat("{}", equalTo(response.toString()))
    }

    @Test(description = "delete запрос на несуществующие эндпоинты", dataProvider = "invalidUsersId")
    fun negativeDeleteUserTest(userId: String) {
        delete(Endpoints.USERS.URL + userId, Status.BAD_REQUEST.code)
        // delete должен возвращать статус 204, если запрашиваемый ресурс существует, и он был успешно удалён
        // здесь delete всегда возвращает 204
    }

    @Test(description = "Удаление пользователя")
    fun deleteUserTest() {
        delete(Endpoints.USERS.URL + "/1", Status.NO_CONTENT.code)
    }

    @DataProvider
    fun updateBody(): Array<Array<String>> {
        return arrayOf(
                arrayOf(UpdateUserModel("morpheus", "zion resident").getBody()), //Позитивные данные из примера
                arrayOf(UpdateUserModel(getRandomString(200), getRandomString(200)).getBody()), // Длинные строки с цифрами
                arrayOf("{}"), //Пустое тело Json
                arrayOf(""), // Без тела
                arrayOf(UpdateUserModel("", "").getBody()), // Пустые значения
                arrayOf("{\"job\": \"morpheus\", \"invalidIntField\": -5}"), // Int значение
                arrayOf("{\"invalidBooleanField\": true, \"name\": \"morpheus\"}"), // Boolean значение
        )
    }

    @Test(description = "Обновление пользователя", dataProvider = "updateBody")
    fun updateUserTest(body: String) {
        val response: JSONObject = patch(Endpoints.USERS.URL + "/1", body, Status.OK.code)
        val updatedUser = Json.decodeFromString(UpdatedUserModel.serializer(), response.toString())
        assertThat(updatedUser.updatedAt, matchesPattern(DataBank.UPDATE_AT_PATTERN.get()))
        val expectedUserJson = if (body != "") JSONObject(body) else JSONObject()
        expectedUserJson.put("updatedAt", updatedUser.updatedAt)
        val expectedUser = Json.decodeFromString(UpdatedUserModel.serializer(), expectedUserJson.toString())
        assertThat(updatedUser, equalTo(expectedUser))
    }

    @Test(description = "Отправка patch запроса с телом не в формате Json")
    fun negativeUpdateUserTest() {
        patch(Endpoints.USERS.URL + "/1", "Не Json", Status.BAD_REQUEST.code)
        //Некорректное тело должно валидироваться на BE, и отдавать в ответе JSON, вместо HTML
    }
}