package com.api


import com.data.DataBank
import com.data.RandomGenerator
import com.models.register.PositiveRegister
import com.models.users.Users
import io.qameta.allure.Step
import kotlinx.serialization.json.Json
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers

open class Generator : Requests(), RandomGenerator {

    @Step("Регистрация в системе нового пользователя")
    fun registerNewUser(login: String, password: String): PositiveRegister {
        post(DataBank.REGISTER_URL.get(), getAuthBody(login, password)).use {
            MatcherAssert.assertThat(it.code, Matchers.equalTo(200))
            return Json.decodeFromString(PositiveRegister.serializer(), it.body!!.string())
        }
    }

    @Step("Получить список всех пользователей с учетом пагинации")
    fun getUsersInPage(page: Int): Users {
        get(DataBank.USERS_URL.get() + "?page=$page").use {
            MatcherAssert.assertThat(it.code, Matchers.equalTo(200))
            return Json.decodeFromString(Users.serializer(), it.body!!.string())
        }
    }
}