package com.tests

import api.data.DataBank
import com.BaseTest
import com.api.models.users.Users
import kotlinx.serialization.json.Json
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.hamcrest.text.MatchesPattern.matchesPattern
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class UsersTest : BaseTest() {

    @DataProvider
    fun positivePages(): Array<Array<Int>> {
        return arrayOf(
                arrayOf(1),
                arrayOf(2),
        )
    }

    @Test(description = "Validate Users", dataProvider = "positivePages")
    fun validateUsersDetailsTest(page: Int) {
        get(DataBank.USERS_URL.get() + "?page=$page").use {
            assertThat(it.code, equalTo(200))
            val users = Json.decodeFromString(Users.serializer(), it.body!!.string())
            assertThat(users.page, equalTo(page))
            assertThat(users.total, greaterThanOrEqualTo(users.per_page))
            assertThat(users.per_page, equalTo(users.data.size))
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
    fun otherPages(): Array<Array<Int>> {
        return arrayOf(
                arrayOf(-1),
                arrayOf(3),
                arrayOf(999),
        )
    }

    @Test(description = "Test other pages", dataProvider = "otherPages")
    fun otherPagesTest(page: Int) {
        get(DataBank.USERS_URL.get() + "?page=$page").use {
            assertThat(it.code, equalTo(200))
            val users = Json.decodeFromString(Users.serializer(), it.body!!.string())
            assertThat(users.per_page, equalTo(0))
            assertThat(users.page, equalTo(page))
            assertThat(users.total, greaterThanOrEqualTo(0))
            assertThat(users.ad.company, equalTo(DataBank.AD_COMPANY.get()))
            assertThat(users.ad.text, equalTo(DataBank.AD_TEXT.get()))
            assertThat(users.ad.url, equalTo(DataBank.AD_URL.get()))
        }
    }

    @Test(description = "Test zero page")
    fun zeroPageTest() {
        get(DataBank.USERS_URL.get() + "?page=0").use {
            assertThat(it.code, equalTo(200))
            val users = Json.decodeFromString(Users.serializer(), it.body!!.string())
            assertThat(users.page, equalTo(1))
            assertThat(users.total, greaterThanOrEqualTo(users.per_page))
            assertThat(users.per_page, equalTo(users.data.size))
        }
    }
}