package com.tests.resources

import com.BaseTest
import com.api.Status
import com.data.DataBank
import com.models.response.resourse.SingleResourceModel
import kotlinx.serialization.json.Json
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.json.JSONObject
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class SingleResourceTest : BaseTest() {

    @DataProvider
    fun positiveResourcesId(): Array<Array<Int>> {
        return arrayOf(
                arrayOf(1),
                arrayOf(12)
        )
    }

    @Test(description = "Валидация деталей ответа для одного ресурса", dataProvider = "positiveResourcesId")
    fun validateSingleResourceDetailsTest(userId: Int) {
        val resourceJson: JSONObject = get(DataBank.RESOURCE_URL.get() + "/$userId", Status.OK.code)
        val resource = Json.decodeFromString(SingleResourceModel.serializer(), resourceJson.toString())
        assertThat(resource.ad.company, equalTo(DataBank.AD_COMPANY.get()))
        assertThat(resource.ad.text, equalTo(DataBank.AD_TEXT.get()))
        assertThat(resource.ad.url, equalTo(DataBank.AD_URL.get()))
        assertThat(resource.data.id, equalTo(userId))
        assertThat(resource.data.name, matchesPattern(DataBank.RESOURCE_NAME_PATTERN.get()))
        assertThat(resource.data.year, allOf(greaterThanOrEqualTo(1900), lessThanOrEqualTo(2100)))
        assertThat(resource.data.color, matchesPattern(DataBank.COLOR_PATTERN.get()))
        assertThat(resource.data.pantone_value, matchesPattern(DataBank.PHONE_PATTERN.get()))
    }

    @DataProvider
    fun invalidResourcesId(): Array<Array<String>> {
        return arrayOf(
                arrayOf("/0"),
                arrayOf("/13"),
                arrayOf("/-1"),
                arrayOf("/resource"),
        )
    }

    @Test(description = "Проверка результатов запроса с неверными параметрами", dataProvider = "invalidResourcesId")
    fun invalidBodyForSingleResourceTest(userId: String) {
        val resourceJson: JSONObject = get(DataBank.RESOURCE_URL.get() + userId, Status.NOT_FOUND.code)
        assertThat("{}", equalTo(resourceJson.toString()))
    }

    @Test(description = "Создание ресурса")
    fun positiveCreateResourceTest() {
        post(DataBank.RESOURCE_URL.get() + "/1", "", Status.OK.code)
    }

    @Test(description = "Обновление ресурса")
    fun positiveUpdateResourceTest() {
        patch(DataBank.RESOURCE_URL.get() + "/1", "", Status.OK.code)
    }

    @Test(description = "Удаление ресурса")
    fun positiveDeleteResourceTest() {
        delete(DataBank.RESOURCE_URL.get() + "/1", Status.NO_CONTENT.code)
    }
}