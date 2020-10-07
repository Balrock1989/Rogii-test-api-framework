package com.tests.users

import com.BaseTest
import com.api.Endpoints
import com.api.Status
import com.data.DataBank
import com.models.general.dataObjects.UserDataModel
import com.models.response.users.ListUsersModel
import io.qameta.allure.Step
import kotlinx.serialization.json.Json
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.hamcrest.text.MatchesPattern.matchesPattern
import org.json.JSONObject
import org.testng.annotations.BeforeClass
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.util.stream.Collectors.toList


class ListUsersTest : BaseTest() {
    private var users = ArrayList<UserDataModel>()
    private val usersPerPage: Int = 6

    @Step("Создание пользователей")
    @BeforeClass(description = "Предположим что нам заранее известны точные данные, которые мы получим с 1 страницы")
    fun prepare() {
        users.add(UserDataModel{it.id=1;it.email="george.bluth@reqres.in";it.firstName="George";it.lastName="Bluth";it.avatar="https://s3.amazonaws.com/uifaces/faces/twitter/calebogden/128.jpg"})
        users.add(UserDataModel{it.id=2;it.email="janet.weaver@reqres.in";it.firstName="Janet";it.lastName="Weaver";it.avatar="https://s3.amazonaws.com/uifaces/faces/twitter/josephstein/128.jpg"})
        users.add(UserDataModel{it.id=3;it.email="emma.wong@reqres.in";it.firstName="Emma";it.lastName="Wong";it.avatar="https://s3.amazonaws.com/uifaces/faces/twitter/olegpogodaev/128.jpg"})
        users.add(UserDataModel{it.id=4;it.email="eve.holt@reqres.in";it.firstName="Eve";it.lastName="Holt";it.avatar="https://s3.amazonaws.com/uifaces/faces/twitter/marcoramires/128.jpg"})
        users.add(UserDataModel{it.id=5;it.email="charles.morris@reqres.in";it.firstName="Charles";it.lastName="Morris";it.avatar="https://s3.amazonaws.com/uifaces/faces/twitter/stephenmoon/128.jpg"})
        users.add(UserDataModel{it.id=6;it.email="tracey.ramos@reqres.in";it.firstName="Tracey";it.lastName="Ramos";it.avatar="https://s3.amazonaws.com/uifaces/faces/twitter/bigmancho/128.jpg"})
    }

    @DataProvider
    fun positivePages(): Array<Array<Int>> {
        return arrayOf(
                arrayOf(1),
                arrayOf(2),
        )
    }

    @Test(description = "Валидация списка пользователей", dataProvider = "positivePages")
    fun validateUsersDetailsTest(page: Int) {
        val response: JSONObject = get(Endpoints.USERS.URL + "?page=$page", Status.OK.code)
        val users = Json.decodeFromString(ListUsersModel.serializer(), response.toString())
        assertThat(users.page, equalTo(page))
        assertThat(users.total, greaterThanOrEqualTo(users.per_page))
        assertThat(users.per_page, equalTo(usersPerPage))
        assertThat(users.ad.company, equalTo(DataBank.AD_COMPANY.get()))
        assertThat(users.ad.text, equalTo(DataBank.AD_TEXT.get()))
        assertThat(users.ad.url, equalTo(DataBank.AD_URL.get()))
        checkSortedUsersById(users.data)
        users.data.forEach { user ->
            assertThat(user.id!!, greaterThanOrEqualTo(1))
            assertThat(user.firstName, matchesPattern(DataBank.USER_NAME_PATTERN.get()))
            assertThat(user.lastName, matchesPattern(DataBank.USER_NAME_PATTERN.get()))
            assertThat(user.email, matchesPattern(DataBank.EMAIL_PATTERN.get()))
            assertThat(user.avatar, matchesPattern(DataBank.URL_PATTERN.get()))
        }
    }

    @Test(description = "Демонстрация теста для валидации через JsonPath")
    fun exampleJsonPathTest() {
        val response: JSONObject = get(Endpoints.USERS.URL + "?page=1", Status.OK.code)
        assertThat(parseJson(response, "data").size, equalTo(response.getJSONArray("data").length()))
        assertThat(parseJson(response, "data[*].id"), equalTo(users.stream().map { user -> user.id }.collect(toList())))
        assertThat(parseJson(response, "data[*].email"), equalTo(users.stream().map { user -> user.email }.collect(toList())))
        assertThat(parseJson(response, "data[*].first_name"), equalTo(users.stream().map { user -> user.firstName }.collect(toList())))
        assertThat(parseJson(response, "data[*].last_name"), equalTo(users.stream().map { user -> user.lastName }.collect(toList())))
        assertThat(parseJson(response, "data[*].avatar"), equalTo(users.stream().map { user -> user.avatar }.collect(toList())))
    }

    @Test(description = "Демонстрация теста для валидации через сравнение объектов")
    fun exampleCompareObjectsTest() {
        val response: JSONObject = get(Endpoints.USERS.URL + "?page=1", Status.OK.code)
        for (i in users.indices) {
            val userInResponse = Json.decodeFromString(UserDataModel.serializer(), response.getJSONArray("data")[i].toString())
            assertThat(users[i], equalTo(userInResponse))
        }
    }

    @DataProvider
    fun emptyPages(): Array<Array<Int>> {
        return arrayOf(
                arrayOf(-1), // падает т.к. на -1 странице не должно быть пользователей
                arrayOf(3),
                arrayOf(999),
        )
    }

    @Test(description = "Проверка пустых страниц", dataProvider = "emptyPages")
    fun otherPagesTest(page: Int) {
        val response: JSONObject = get(Endpoints.USERS.URL + "?page=$page", Status.OK.code)
        val users = Json.decodeFromString(ListUsersModel.serializer(), response.toString())
        assertThat(users.data, hasSize(0))
        assertThat(users.page, equalTo(page))
        assertThat(users.per_page, equalTo(usersPerPage))
        assertThat(users.total, greaterThanOrEqualTo(0))
        assertThat(users.ad.company, equalTo(DataBank.AD_COMPANY.get()))
        assertThat(users.ad.text, equalTo(DataBank.AD_TEXT.get()))
        assertThat(users.ad.url, equalTo(DataBank.AD_URL.get()))
    }

    @DataProvider
    fun otherPages(): Array<Array<String>> {
        return arrayOf(
                arrayOf("?page=0"),
                arrayOf("?page=two"),
                arrayOf(""),
        )
    }

    @Test(description = "Проверка нулевой старницы", dataProvider = "otherPages")
    fun firstPageTest(search: String) {
        val response: JSONObject = get(Endpoints.USERS.URL + search, Status.OK.code)
        val users = Json.decodeFromString(ListUsersModel.serializer(), response.toString())
        assertThat(users.data.size, not(equalTo(0)))
        assertThat(users.page, equalTo(1))
    }
}