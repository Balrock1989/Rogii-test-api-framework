package com.api


import com.data.DataBank
import com.data.RandomGenerator
import com.models.request.RegisterBody
import com.models.response.register.PositiveRegister
import com.models.response.users.Users
import io.qameta.allure.Step
import kotlinx.serialization.json.Json

open class Generator : Requests(), RandomGenerator {

    @Step("Регистрация в системе нового пользователя")
    fun registerNewUser(login: String, password: String): PositiveRegister {
        post(DataBank.REGISTER_URL.get(), RegisterBody(login, password).getBody()).use {
            checkStatus(it.code, 200)
            return Json.decodeFromString(PositiveRegister.serializer(), it.body!!.string())
        }
    }

    @Step("Получить список всех пользователей с учетом пагинации")
    fun getUsersInPage(page: Int): Users {
        get(DataBank.USERS_URL.get() + "?page=$page").use {
            checkStatus(it.code, 200)
            return Json.decodeFromString(Users.serializer(), it.body!!.string())
        }
    }
}