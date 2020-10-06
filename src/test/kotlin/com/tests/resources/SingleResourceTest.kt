package com.tests.resources

import com.BaseTest
import com.api.Endpoints
import com.api.Status
import com.data.DataBank
import com.models.request.resource.UpdateResourceModel
import com.models.response.resource.SingleResourceModel
import com.models.response.resource.UpdatedResourceModel
import kotlinx.serialization.json.Json
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.hamcrest.text.MatchesPattern
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
    fun validateSingleResourceDetailsTest(resourceId: Int) {
        val response: JSONObject = get(Endpoints.RESOURCE.URL + "/$resourceId", Status.OK.code)
        val resource = Json.decodeFromString(SingleResourceModel.serializer(), response.toString())
        assertThat(resource.ad.company, equalTo(DataBank.AD_COMPANY.get()))
        assertThat(resource.ad.text, equalTo(DataBank.AD_TEXT.get()))
        assertThat(resource.ad.url, equalTo(DataBank.AD_URL.get()))
        assertThat(resource.data.id, equalTo(resourceId))
        assertThat(resource.data.name, matchesPattern(DataBank.RESOURCE_NAME_PATTERN.get()))
        assertThat(resource.data.year, allOf(greaterThanOrEqualTo(1900), lessThanOrEqualTo(2100)))
        assertThat(resource.data.color, matchesPattern(DataBank.COLOR_PATTERN.get()))
        assertThat(resource.data.pantone, matchesPattern(DataBank.PHONE_PATTERN.get()))
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
    fun invalidBodyForSingleResourceTest(resourceId: String) {
        val response: JSONObject = get(Endpoints.RESOURCE.URL + resourceId, Status.NOT_FOUND.code)
        assertThat("{}", equalTo(response.toString()))
    }

    @Test(description = "delete запрос на несуществующие эндпоинты", dataProvider = "invalidResourcesId")
    fun negativeDeleteResourceTest(resourceId: String) {
        delete(Endpoints.RESOURCE.URL + resourceId, Status.BAD_REQUEST.code) // падает потому что delete запрос на любой эндпоинт возвращает 204
    }

    @Test(description = "Удаление ресурса")
    fun deleteResourceTest() {
        delete(Endpoints.RESOURCE.URL + "/1", Status.NO_CONTENT.code)
    }

    @DataProvider
    fun updateBody(): Array<Array<String>> {
        return arrayOf(
                arrayOf(UpdateResourceModel("morpheus1950", "zion resident").getBody()),
                arrayOf(UpdateResourceModel(getRandomString(200), getRandomString(200)).getBody()),
                arrayOf("{}"),
                arrayOf(""),
                arrayOf(UpdateResourceModel("", "").getBody()),
                arrayOf("{\"job\": \"morpheus\", \"invalidIntField\": -5}"),
                arrayOf("{\"invalidBooleanField\": true, \"name\": \"morpheus\"}"),
        )
    }

    @Test(description = "Обновление ресурса", dataProvider = "updateBody")
    fun updateResourceTest(body: String) {
        val response: JSONObject = patch(Endpoints.RESOURCE.URL + "/1", body, Status.OK.code)
        val updatedResource = Json.decodeFromString(UpdatedResourceModel.serializer(), response.toString())
        assertThat(updatedResource.updatedAt, MatchesPattern.matchesPattern(DataBank.UPDATE_AT_PATTERN.get()))
        val expectedResourceJson = if (body != "") JSONObject(body) else JSONObject()
        expectedResourceJson.put("updatedAt",updatedResource.updatedAt)
        val expectedResource = Json.decodeFromString(UpdatedResourceModel.serializer(), expectedResourceJson.toString())
        assertThat(updatedResource, equalTo(expectedResource))
    }

    @Test(description = "Отправка patch запроса с телом не в формате Json")
    fun negativeUpdateResourceTest() {
        patch(Endpoints.RESOURCE.URL + "/1", "Не Json", Status.BAD_REQUEST.code) // падает т.к. возвращается не Json
    }
}