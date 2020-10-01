package com.tests.users

import com.BaseTest
import com.api.Status
import com.data.DataBank
import com.models.response.users.ListUsersModel
import kotlinx.serialization.json.Json
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.hamcrest.text.MatchesPattern.matchesPattern
import org.json.JSONObject
import org.testng.annotations.DataProvider
import org.testng.annotations.Test


class ListUsersTest : BaseTest() {
    private val usersPerPage: Int = 6

    @DataProvider
    fun positivePages(): Array<Array<Int>> {
        return arrayOf(
                arrayOf(1),
                arrayOf(2),
        )
    }

    @Test(description = "Валидация списка пользователей", dataProvider = "positivePages")
    fun validateUsersDetailsTest(page: Int) {
        val usersJson: JSONObject = get(DataBank.USERS_URL.get() + "?page=$page", Status.OK.code)
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
        val usersJson: JSONObject = get(DataBank.USERS_URL.get() + "?page=$page", Status.OK.code)
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
        val usersJson: JSONObject = get(DataBank.USERS_URL.get() + search, Status.OK.code)
        val users = Json.decodeFromString(ListUsersModel.serializer(), usersJson.toString())
        assertThat(users.data.size, not(equalTo(0)))
        assertThat(users.page, equalTo(1))
    }
}