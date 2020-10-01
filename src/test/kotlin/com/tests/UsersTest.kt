package com.tests

import com.data.DataBank
import com.BaseTest
import com.models.users.Users
import kotlinx.serialization.json.Json
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.hamcrest.text.MatchesPattern.matchesPattern
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class UsersTest : BaseTest() {
    val USERS_PER_PAGE: Int = 6

    @DataProvider
    fun positivePages(): Array<Array<Int>> {
        return arrayOf(
                arrayOf(1),
                arrayOf(2),
        )
    }

    @Test(description = "Валидация списка пользователей", dataProvider = "positivePages")
    fun validateUsersDetailsTest(page: Int) {
        get(DataBank.USERS_URL.get() + "?page=$page").use {
            assertThat(it.code, equalTo(200))
            val users = Json.decodeFromString(Users.serializer(), it.body!!.string())
            assertThat(users.page, equalTo(page))
            assertThat(users.total, greaterThanOrEqualTo(users.per_page))
            assertThat(users.per_page, equalTo(USERS_PER_PAGE))
            assertThat(users.ad.company, equalTo(DataBank.AD_COMPANY.get()))
            assertThat(users.ad.text, equalTo(DataBank.AD_TEXT.get()))
            assertThat(users.ad.url, equalTo(DataBank.AD_URL.get()))
            users.data.forEach { user ->
                assertThat(user.id, greaterThanOrEqualTo(1))
                assertThat(user.first_name, matchesPattern("[A-Z]\\D{2,20}"))
                assertThat(user.last_name, matchesPattern("[A-Z]\\D{2,20}"))
                assertThat(user.email, matchesPattern("^\\w*\\.\\w*@\\w*.in"))
                assertThat(user.avatar, matchesPattern("^http?s:\\/\\/\\S*\\.jpg"))
            }
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
        get(DataBank.USERS_URL.get() + "?page=$page").use {
            assertThat(it.code, equalTo(200))
            val users = Json.decodeFromString(Users.serializer(), it.body!!.string())
            assertThat(users.data, hasSize(0))
            assertThat(users.ad.company, equalTo(DataBank.AD_COMPANY.get()))
            assertThat(users.ad.text, equalTo(DataBank.AD_TEXT.get()))
            assertThat(users.ad.url, equalTo(DataBank.AD_URL.get()))
            assertThat(users.per_page, equalTo(USERS_PER_PAGE))
            assertThat(users.page, equalTo(page))
            assertThat(users.total, greaterThanOrEqualTo(0))
        }
    }

    @DataProvider
    fun otherPages(): Array<Array<Any>> {
        return arrayOf(
                arrayOf("?page=0", 1),
                arrayOf("", 1),
        )
    }

    @Test(description = "Проверка нулевой старницы", dataProvider = "otherPages")
    fun zeroPageTest(search: String, page: Int) {
        get(DataBank.USERS_URL.get() + search).use {
            assertThat(it.code, equalTo(200))
            val users = Json.decodeFromString(Users.serializer(), it.body!!.string())
            assertThat(users.data.size, greaterThanOrEqualTo(1))
            assertThat(users.page, equalTo(page))
        }
    }
}