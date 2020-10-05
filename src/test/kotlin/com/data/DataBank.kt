package com.data

/*** Класс с данными*/
enum class DataBank {
    LOGIN_ADMIN("eve.holt@reqres.in"),
    PASSWORD_ADMIN("pistol"),
    AD_COMPANY("StatusCode Weekly"),
    AD_TEXT("A weekly newsletter focusing on software development, infrastructure, the server, performance, and the stack end of things."),
    AD_URL("http://statuscode.org/"),
    USER_NAME_PATTERN("[A-Z]\\D{2,20}"),
    RESOURCE_NAME_PATTERN("[a-z]\\D{2,20}"),
    EMAIL_PATTERN("^\\w*\\.\\w*@\\w*.in"),
    COLOR_PATTERN("#[0-9A-Z]{6}"),
    PHONE_PATTERN("\\d{2}-\\d{4}"),
    UPDATE_AT_PATTERN("\\d{4}-\\d{2}-\\S{5}:\\d{2}:\\d{2}\\.\\S{4}"),
    URL_PATTERN("^http?s:\\/\\/\\S*\\.jpg");

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