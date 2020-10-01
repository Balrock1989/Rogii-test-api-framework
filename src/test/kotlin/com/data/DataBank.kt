package com.data

enum class DataBank {
    LOGIN_ADMIN("eve.holt@reqres.in"),
    PASSWORD_ADMIN("pistol"),
    REGISTER_URL("https://reqres.in/api/register"),
    USERS_URL("https://reqres.in/api/users"),
    AD_COMPANY("StatusCode Weekly"),
    AD_TEXT("A weekly newsletter focusing on software development, infrastructure, the server, performance, and the stack end of things."),
    AD_URL("http://statuscode.org/");

    private lateinit var array: List<String>
    private lateinit var value: String

    constructor(strings: List<String>) {
        array = strings
    }

    constructor(text: String) {
        value = text
    }

    fun get(): String {
        return value
    }
}