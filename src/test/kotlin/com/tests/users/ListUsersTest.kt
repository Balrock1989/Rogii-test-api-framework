package com.tests.users

import com.BaseTest
import com.api.Endpoints
import com.api.Status
import com.data.DataBank
import com.jayway.jsonpath.JsonPath.parse
import com.models.request.users.UserModel
import com.models.response.users.ListUsersModel
import kotlinx.serialization.json.Json
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.hamcrest.text.MatchesPattern.matchesPattern
import org.json.JSONObject
import org.testng.annotations.BeforeClass
import org.testng.annotations.DataProvider
import org.testng.annotations.Test


class ListUsersTest : BaseTest() {
    private lateinit var user1: UserModel
    private lateinit var user2: UserModel
    private lateinit var user3: UserModel
    private lateinit var user4: UserModel
    private lateinit var user5: UserModel
    private lateinit var user6: UserModel
    private val usersPerPage: Int = 6

    @BeforeClass
    fun prepare() {
        user1 = UserModel(1, "george.bluth@reqres.in", "George", "Bluth", "https://s3.amazonaws.com/uifaces/faces/twitter/calebogden/128.jpg")
        user2 = UserModel(2, "janet.weaver@reqres.in", "Janet", "Weaver", "https://s3.amazonaws.com/uifaces/faces/twitter/josephstein/128.jpg")
        user3 = UserModel(3, "emma.wong@reqres.in", "Emma", "Wong", "https://s3.amazonaws.com/uifaces/faces/twitter/olegpogodaev/128.jpg")
        user4 = UserModel(4, "eve.holt@reqres.in", "Eve", "Holt", "https://s3.amazonaws.com/uifaces/faces/twitter/marcoramires/128.jpg")
        user5 = UserModel(5, "charles.morris@reqres.in", "Charles", "Morris", "https://s3.amazonaws.com/uifaces/faces/twitter/stephenmoon/128.jpg")
        user6 = UserModel(6, "tracey.ramos@reqres.in", "Tracey", "Ramos", "https://s3.amazonaws.com/uifaces/faces/twitter/bigmancho/128.jpg")
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
        val usersJson: JSONObject = get(Endpoints.USERS.URL + "?page=$page", Status.OK.code)
        val users = Json.decodeFromString(ListUsersModel.serializer(), usersJson.toString())
        assertThat(users.page, equalTo(page))
        assertThat(users.total, greaterThanOrEqualTo(users.per_page))
        assertThat(users.per_page, equalTo(usersPerPage))
        assertThat(users.ad.company, equalTo(DataBank.AD_COMPANY.get()))
        assertThat(users.ad.text, equalTo(DataBank.AD_TEXT.get()))
        assertThat(users.ad.url, equalTo(DataBank.AD_URL.get()))
        checkSortedUsersById(users.data)
        users.data.forEach { user ->
            assertThat(user.id, greaterThanOrEqualTo(1))
            assertThat(user.first_name, matchesPattern(DataBank.USER_NAME_PATTERN.get()))
            assertThat(user.last_name, matchesPattern(DataBank.USER_NAME_PATTERN.get()))
            assertThat(user.email, matchesPattern(DataBank.EMAIL_PATTERN.get()))
            assertThat(user.avatar, matchesPattern(DataBank.URL_PATTERN.get()))
        }
    }

    @Test(description = "Предположим что нам заранее известны точные данные, которые мы получим с 1 страницы")
    fun exampleJsonPathTest() {
        val usersJson: JSONObject = get(Endpoints.USERS.URL + "?page=1", Status.OK.code)
        assertThat(parse(usersJson.toString()).read("$..first_name") as List<String>, contains(user1.firstName, user2.firstName, user3.firstName, user4.firstName, user5.firstName, user6.firstName))
        //TODO доработать проверки
    }

    @DataProvider
    fun emptyPages(): Array<Array<Int>> {
        return arrayOf(
                arrayOf(-1),
                arrayOf(3),
                arrayOf(999),
        )
    }

    @Test(description = "Проверка пустых страниц", dataProvider = "emptyPages")
    fun otherPagesTest(page: Int) {
        val usersJson: JSONObject = get(Endpoints.USERS.URL + "?page=$page", Status.OK.code)
        val users = Json.decodeFromString(ListUsersModel.serializer(), usersJson.toString())
        assertThat(users.data, hasSize(0))
        assertThat(users.ad.company, equalTo(DataBank.AD_COMPANY.get()))
        assertThat(users.ad.text, equalTo(DataBank.AD_TEXT.get()))
        assertThat(users.ad.url, equalTo(DataBank.AD_URL.get()))
        assertThat(users.per_page, equalTo(usersPerPage))
        assertThat(users.page, equalTo(page))
        assertThat(users.total, greaterThanOrEqualTo(0))
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
        val usersJson: JSONObject = get(Endpoints.USERS.URL + search, Status.OK.code)
        val users = Json.decodeFromString(ListUsersModel.serializer(), usersJson.toString())
        assertThat(users.data.size, not(equalTo(0)))
        assertThat(users.page, equalTo(1))
    }
}