package com.tests.resources

import com.BaseTest
import com.api.Endpoints
import com.api.Status
import com.data.DataBank
import com.models.response.resourse.ListResourceModel
import com.models.response.users.ListUsersModel
import kotlinx.serialization.json.Json
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.json.JSONObject
import org.testng.annotations.DataProvider
import org.testng.annotations.Test


class ListResourceTest : BaseTest() {
    private val resourcesPerPage: Int = 6

    @DataProvider
    fun positivePages(): Array<Array<Int>> {
        return arrayOf(
                arrayOf(1),
                arrayOf(2),
        )
    }

    @Test(description = "Валидация списка ресурсов", dataProvider = "positivePages")
    fun validateResourcesDetailsTest(page: Int) {
        val usersJson: JSONObject = get(Endpoints.RESOURCE.URL + "?page=$page", Status.OK.code)
        val resources = Json.decodeFromString(ListResourceModel.serializer(), usersJson.toString())
        assertThat(resources.page, equalTo(page))
        assertThat(resources.total, greaterThanOrEqualTo(resources.per_page))
        assertThat(resources.per_page, equalTo(resourcesPerPage))
        assertThat(resources.ad.company, equalTo(DataBank.AD_COMPANY.get()))
        assertThat(resources.ad.text, equalTo(DataBank.AD_TEXT.get()))
        assertThat(resources.ad.url, equalTo(DataBank.AD_URL.get()))
        assertThat(resources.data.size, greaterThanOrEqualTo(1))
        checkSortedResourcesById(resources.data)
        resources.data.forEach { resource ->
            assertThat(resource.id!!, greaterThanOrEqualTo(1))
            assertThat(resource.name, matchesPattern(DataBank.RESOURCE_NAME_PATTERN.get()))
            assertThat(resource.year, allOf(greaterThanOrEqualTo(1900), lessThanOrEqualTo(2100)))
            assertThat(resource.color, matchesPattern(DataBank.COLOR_PATTERN.get()))
            assertThat(resource.pantone, matchesPattern(DataBank.PHONE_PATTERN.get()))
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
        val usersJson: JSONObject = get(Endpoints.RESOURCE.URL + "?page=$page", Status.OK.code)
        val resources = Json.decodeFromString(ListResourceModel.serializer(), usersJson.toString())
        assertThat(resources.page, equalTo(page))
        assertThat(resources.total, greaterThanOrEqualTo(resources.per_page))
        assertThat(resources.per_page, equalTo(resourcesPerPage))
        assertThat(resources.ad.company, equalTo(DataBank.AD_COMPANY.get()))
        assertThat(resources.ad.text, equalTo(DataBank.AD_TEXT.get()))
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
        val resourcesJson: JSONObject = get(Endpoints.USERS.URL + search, Status.OK.code)
        val resources = Json.decodeFromString(ListUsersModel.serializer(), resourcesJson.toString())
        assertThat(resources.data.size, not(equalTo(0)))
        assertThat(resources.page, equalTo(1))
    }
}