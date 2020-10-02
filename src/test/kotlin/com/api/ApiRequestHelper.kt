package com.api

import com.models.response.resourse.ResourceDataModel
import com.models.response.users.UserDataModel
import io.qameta.allure.Step
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import java.util.stream.Collectors


/*** Вспомогательные методы для работы с API*/
open class ApiRequestHelper : HTTPClient() {


    @Step("Проверка кода ответа сервера")
    fun checkStatus(code: Int, expectedCode: Int) {
        assertThat(code, equalTo(expectedCode))
    }

    @Step("Проверка сортировки пользователей по Id")
    fun checkSortedUsersById(users: List<UserDataModel>) {
        val usersId: List<Int> = users.stream().map { user -> user.id }.collect(Collectors.toList())
        assertThat(usersId, equalTo(usersId.sorted()))
    }

    @Step("Проверка сортировки ресурсов по Id")
    fun checkSortedResourcesById(resources: List<ResourceDataModel>) {
        val resourcesId: List<Int> = resources.stream().map { resource -> resource.id }.collect(Collectors.toList())
        assertThat(resourcesId, equalTo(resourcesId.sorted()))
    }
}