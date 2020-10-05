package com.tests.resources

import com.BaseTest
import com.api.Endpoints
import com.api.Status
import com.data.DataBank
import com.models.request.users.UpdateUserModel
import com.models.response.resourse.SingleResourceModel
import com.models.response.resourse.UpdatedResourseModel
import com.models.response.users.UpdatedUserModel
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
    fun validateSingleResourceDetailsTest(userId: Int) {
        val response: JSONObject = get(Endpoints.RESOURCE.URL + "/$userId", Status.OK.code)
        val resource = Json.decodeFromString(SingleResourceModel.serializer(), response.toString())
        assertThat(resource.ad.company, equalTo(DataBank.AD_COMPANY.get()))
        assertThat(resource.ad.text, equalTo(DataBank.AD_TEXT.get()))
        assertThat(resource.ad.url, equalTo(DataBank.AD_URL.get()))
        assertThat(resource.data.id, equalTo(userId))
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
    fun invalidBodyForSingleResourceTest(userId: String) {
        val response: JSONObject = get(Endpoints.RESOURCE.URL + userId, Status.NOT_FOUND.code)
        assertThat("{}", equalTo(response.toString()))
    }

    @DataProvider
    fun updateBody(): Array<Array<String>> {
        return arrayOf(
                arrayOf(UpdateUserModel("morpheus1950", "zion resident").getBody()),
                arrayOf(UpdateUserModel(getRandomString(200), getRandomString(200)).getBody()),
                arrayOf("{}"),
                arrayOf(""),
                arrayOf(UpdateUserModel("", "").getBody()),
                arrayOf("{\"job\": \"morpheus\", \"invalidIntField\": -5}"),
                arrayOf("{\"invalidBooleanField\": true, \"name\": \"morpheus\"}"),
        )
    }

    @Test(description = "Обновление ресурса", dataProvider = "updateBody")
    fun updateResourseTest(body: String) {
        val response: JSONObject = patch(Endpoints.RESOURCE.URL + "/1", body, Status.OK.code)
        val updatedResourse = Json.decodeFromString(UpdatedResourseModel.serializer(), response.toString())
        assertThat(updatedResourse.updatedAt, MatchesPattern.matchesPattern(DataBank.UPDATE_AT_PATTERN.get()))
        val expectedResourseJson = if (body != "") JSONObject(body) else JSONObject()
        expectedResourseJson.put("updatedAt",updatedResourse.updatedAt)
        val expectedResourse = Json.decodeFromString(UpdatedResourseModel.serializer(), expectedResourseJson.toString())
        assertThat(updatedResourse, equalTo(expectedResourse))
    }

    @Test(description = "Отправка patch запроса с телом не в формате Json")
    fun negativeUpdateUserTest() {
        patch(Endpoints.RESOURCE.URL + "/1", "Не Json", Status.BAD_REQUEST.code) // падает т.к. возвращается не Json
    }

    @Test(description = "Удаление ресурса", dataProvider = "invalidResourcesId")
    fun positiveDeleteResourceTest(userId: String) {
        delete(Endpoints.RESOURCE.URL + userId, Status.NO_CONTENT.code)
    }
}