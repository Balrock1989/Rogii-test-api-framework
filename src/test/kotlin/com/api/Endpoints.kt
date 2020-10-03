package com.api

enum class Endpoints(val URL: String) {
    REGISTER(System.getProperty("base.url") + "/register"),
    USERS(System.getProperty("base.url") + "/users"),
    RESOURCE(System.getProperty("base.url") + "/unknown");

}