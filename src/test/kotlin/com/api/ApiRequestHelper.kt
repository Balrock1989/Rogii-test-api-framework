package com.api

import com.jayway.jsonpath.JsonPath
import com.models.general.dataObjects.ResourceDataModel
import com.models.general.dataObjects.UserDataModel
import io.qameta.allure.Step
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.json.JSONObject
import java.util.stream.Collectors.*


/*** Вспомогательные методы для работы с API*/
open class ApiRequestHelper : HTTPClient() {

    /*** Вспомогательный метод для работы с JsonPath*/
    fun parseJson(json: JSONObject, path: String): List<String>{
        return JsonPath.parse(json.toString()).read("$.$path") as List<String>
    }

    @Step("Проверка кода ответа сервера")
    fun checkStatus(code: Int, expectedCode: Int) {
        assertThat(code, equalTo(expectedCode))
    }

    @Step("Проверка сортировки пользователей по Id")
    fun checkSortedUsersById(users: List<UserDataModel>) {
        val usersId: List<Int> = users.stream().map { user -> user.id!! }.collect(toList())
        assertThat(usersId, equalTo(usersId.sorted()))
    }

    @Step("Проверка сортировки ресурсов по Id")
    fun checkSortedResourcesById(resources: List<ResourceDataModel>) {
        val resourcesId: List<Int> = resources.stream().map { resource -> resource.id!! }.collect(toList())
        assertThat(resourcesId, equalTo(resourcesId.sorted()))
    }
}