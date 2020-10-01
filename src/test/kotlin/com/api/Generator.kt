package com.api


import com.data.DataBank
import com.data.RandomGenerator
import com.models.request.RegisterBody
import com.models.response.register.PositiveRegister
import com.models.response.users.Users
import io.qameta.allure.Step
import kotlinx.serialization.json.Json
import org.json.JSONObject

open class Generator : Requests(), RandomGenerator {

    @Step("Регистрация в системе нового пользователя")
    fun registerNewUser(login: String, password: String): PositiveRegister {
        val usersJson: JSONObject = post(DataBank.REGISTER_URL.get(), RegisterBody(login, password).getBody(), Status.OK.code)
        return Json.decodeFromString(PositiveRegister.serializer(), usersJson.toString())
    }


    @Step("Получить список всех пользователей с учетом пагинации")
    fun getUsersInPage(page: Int): Users {
        val usersJson: JSONObject = get(DataBank.USERS_URL.get() + "?page=$page", Status.OK.code)
        return Json.decodeFromString(Users.serializer(), usersJson.toString())
    }
}