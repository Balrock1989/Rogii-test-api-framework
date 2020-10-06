package com.data


import com.api.Endpoints
import com.api.Requests
import com.api.Status
import com.models.general.dataObjects.BaseModel
import com.models.general.dataObjects.ResourceDataModel
import com.models.general.dataObjects.UserDataModel
import com.models.request.register.RegisterModel
import com.models.response.register.PositiveRegisterModel
import com.models.response.users.ListUsersModel
import io.qameta.allure.Step
import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


/*** Генератор тестовых данных*/
open class DataGenerator : Requests(), RandomStringGenerator {
    private val createdObjects: LinkedHashMap<BaseModel<*>, String> = LinkedHashMap()

    private fun <T : BaseModel<T>> addObject(obj: T, resourcePath: String) {
        createdObjects[obj] = resourcePath
    }

    @Step("Регистрация в системе нового пользователя")
    fun registerNewUser(login: String, password: String): PositiveRegisterModel {
        val usersJson: JSONObject = post(Endpoints.REGISTER.URL, RegisterModel(login, password).getBody(), Status.OK.code)
        return Json.decodeFromString(PositiveRegisterModel.serializer(), usersJson.toString())
    }

    @Step("Получить список всех пользователей с учетом пагинации")
    fun getUsersInPage(page: Int): ListUsersModel {
        val usersJson: JSONObject = get(Endpoints.USERS.URL + "?page=$page", Status.OK.code)
        return Json.decodeFromString(ListUsersModel.serializer(), usersJson.toString())
    }

    @Step("Создание в системе нового пользователя")
    fun newUser(): UserDataModel {
        val user = create(Endpoints.USERS.URL, UserDataModel())
        addObject(user, Endpoints.USERS.URL)
        return user
    }

    @Step("Создание в системе нового ресурса")
    fun newResource(): ResourceDataModel {
        val resource = create(Endpoints.RESOURCE.URL, ResourceDataModel())
        addObject(resource, Endpoints.RESOURCE.URL)
        return resource
    }

    @Step("Очистка тестовых данных")
    fun cleanObjects() {
        val keysObjects: ArrayList<BaseModel<*>> = ArrayList(createdObjects.keys)
        keysObjects.reversed()
        for (obj in keysObjects) {
            val path: String? = createdObjects[obj]
            delete(path + "/${obj.id}", Status.NO_CONTENT.code)
        }
        createdObjects.clear()
    }
}